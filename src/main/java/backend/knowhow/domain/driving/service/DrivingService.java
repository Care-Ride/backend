package backend.knowhow.domain.driving.service;

import backend.knowhow.domain.driving.domain.DrivingSession;
import backend.knowhow.domain.driving.domain.WeatherCondition;
import backend.knowhow.domain.driving.dto.request.DriveEndRequest;
import backend.knowhow.domain.driving.dto.request.LocationRequest;
import backend.knowhow.domain.driving.dto.response.*;
import backend.knowhow.domain.driving.dto.response.weather.KmaUltraSrtNcstResponse;
import backend.knowhow.domain.driving.dto.response.kakao.KakaoPlaceSearchResponse;
import backend.knowhow.domain.driving.dto.summary.DrivingSessionSummary;
import backend.knowhow.domain.driving.repository.DrivingSessionRepository;
import backend.knowhow.domain.driving.service.kakaoMap.KakaoApiClient;
import backend.knowhow.domain.driving.service.weather.KmaWeatherClient;
import backend.knowhow.domain.driving.service.weather.WeatherConditionMapper;
import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrivingService {

    private final KmaWeatherClient kmaWeatherClient;
    private final KakaoApiClient kakaoApiClient;
    private final DrivingSessionRepository drivingSessionRepository;
    private final MemberRepository memberRepository;
    private final WeatherConditionMapper weatherConditionMapper;

    private static final LocalTime NIGHT_START = LocalTime.of(20, 0);   // 20:00
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);  // 6:00
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Transactional(readOnly = true)
    public BeforeDriveDangerResponse getDangerBeforeDrive(LocationRequest request) {
        // 현재 위치 기준 현재 날씨 조회
        KmaUltraSrtNcstResponse ultraSrtNcst = kmaWeatherClient.getUltraSrtNcst(request.getLat(), request.getLon());
        WeatherCondition weatherCondition = weatherConditionMapper.fromUltraSrtNcst(ultraSrtNcst);  // 날씨 상태 enum값

        // 현재 시간 기준 야간운전인지 여부 확인
        LocalTime now = LocalTime.now();
        boolean isNightNow = now.isAfter(NIGHT_START) || now.isBefore(NIGHT_END);
        
        return new BeforeDriveDangerResponse(isNightNow, weatherCondition);
    }

    @Transactional(readOnly = true)
    public PlaceSearchListResponse searchDst(String keyword) {
        // 카카오 키워드 기반 장소 검색 api 호출
        KakaoPlaceSearchResponse kakaoResponse = kakaoApiClient.searchByKeyword(keyword);

        if (kakaoResponse == null || kakaoResponse.getDocuments() == null) {
            return new PlaceSearchListResponse(List.of());
        }

        List<PlaceSearchListResponse.PlaceSearch> results = kakaoResponse.getDocuments()
                .stream()
                .map(doc -> PlaceSearchListResponse.PlaceSearch.builder()
                        .name(doc.getPlaceName())
                        .address(doc.getAddressName())
                        .roadAddress(doc.getRoadAddressName())
                        .lat(doc.getY())
                        .lon(doc.getX())
                        .build())
                .toList();
        return new PlaceSearchListResponse(results);
    }

    @Transactional
    public DriveStartResponse startDriving(Long memberId, LocationRequest request) {
        Member driver = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        DrivingSession drive = DrivingSession.start(driver, request.getLat(), request.getLon());
        DrivingSession saveDrive = drivingSessionRepository.save(drive);
        return new DriveStartResponse(saveDrive.getId(), saveDrive.getStartTime());
    }

    @Transactional
    public DrivingSessionSummary endDriving(Long memberId, DriveEndRequest request) {
        Member driver = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));
        DrivingSession driveSession = drivingSessionRepository.findById(request.getDriveId())
                .orElseThrow(() -> new BaseException(ErrorType.DRIVE_SESSION_NOT_FOUND));

        // driveSession 운전자와 로그인한 유저가 다른 경우 에러
        if(!driveSession.getDriver().equals(driver))
            throw new BaseException(ErrorType.DRIVE_ACCESS_DENIED);
        // 이미 종료처리가 되어있는 경우 에러 처리
        if(driveSession.getEndTime() != null){
            throw new BaseException(ErrorType.DRIVE_ALREADY_ENDED);
        }

        int driveScore = calculateDrivingScore(request.getHardAccelCount(), request.getHardDecelCount(), request.getTotalDistance());

        driveSession.finish(request.getTotalDistance(), request.getHardAccelCount(), request.getHardDecelCount(),
                request.getLat(), request.getLon(), driveScore);

        return DrivingSessionSummary.from(driveSession);
    }

    @Transactional(readOnly = true)
    public DailyDrivingListResponse getDailyDrivingRecords(LocalDate date, Long memberId) {
        Member driver = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));
        List<DrivingSession> drivingList = drivingSessionRepository.findAllByDriverIdAndStartTimeBetween(driver.getId(), date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        List<DrivingSessionSummary> dtoList = drivingList.stream()
                .map(DrivingSessionSummary::from)
                .collect(Collectors.toList());

        return new DailyDrivingListResponse(dtoList, date);
    }

    @Transactional(readOnly = true)
    public MonthlyDriveResponse getMonthlyDrivingRecords(String yearMonth, Long memberId) {
        Member driver = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        YearMonth month;
        try{
            month = YearMonth.parse(yearMonth, YEAR_MONTH_FORMATTER);
        } catch (DateTimeParseException e){
            throw new BaseException(ErrorType.INVALID_DATE_FORMAT);
        }
        LocalDateTime startTime = month.atDay(1).atStartOfDay();
        LocalDateTime endTime = month.plusMonths(1).atDay(1).atStartOfDay();    //다음달 1일 00:00

        // 운전 완료되지 않은 경우 제외한 운전 목록
        List<DrivingSession> drivingList = drivingSessionRepository.findAllByDriverIdAndStartTimeBetweenAndEndTimeIsNotNull(driver.getId(), startTime, endTime);

        int hardAccelSum = drivingList.stream().mapToInt(DrivingSession::getHardAccelCount).sum();
        int hardDecelSum = drivingList.stream().mapToInt(DrivingSession::getHardDecelCount).sum();

        double avgDrivingScore = drivingList.stream()
                .filter(Objects::nonNull)
                .mapToInt(DrivingSession::getScore).average().orElse(0.0);
        avgDrivingScore = Math.round(avgDrivingScore * 10) / 10.0;  // 소숫점 한자리까지

        double totalDistance = drivingList.stream()
                .filter(Objects::nonNull)
                .mapToDouble(DrivingSession::getDistance).sum();

        int hardAccelStar = drivingEventStarCalculator(hardAccelSum, totalDistance);
        int hardDecelStar = drivingEventStarCalculator(hardDecelSum, totalDistance);

        return new MonthlyDriveResponse(month.getMonthValue(), avgDrivingScore, hardAccelStar, hardDecelStar);
    }

    // 운전 종료 시 급가속, 급감속 기반 점수 산정
    private int calculateDrivingScore(int hardAccel, int hardDecel, double distance){
        double ratePer100km = (hardAccel + hardDecel) / distance * 100.0;
        return (int) Math.round(100-ratePer100km);
    }

    // 급가속, 급가속 월별 점수 계산
    private int drivingEventStarCalculator(int eventCount, double totalDistance){
        if(totalDistance <= 0.0) return 0;

        double ratePer100km = (eventCount / totalDistance) * 100.0; // 100km당 event 발생 비율

        if(ratePer100km <= 2.0) return 5;
        else if(ratePer100km <= 5.0) return 4;
        else if(ratePer100km <= 9.0) return 3;
        else if(ratePer100km <= 14.0) return 2;
        else return 1;
    }
}

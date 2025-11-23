package backend.knowhow.domain.driving.service;

import backend.knowhow.domain.driving.domain.DrivingSession;
import backend.knowhow.domain.driving.domain.WeatherCondition;
import backend.knowhow.domain.driving.dto.request.LocationRequest;
import backend.knowhow.domain.driving.dto.response.BeforeDriveDangerResponse;
import backend.knowhow.domain.driving.dto.response.DriveStartResponse;
import backend.knowhow.domain.driving.dto.response.weather.KmaUltraSrtNcstResponse;
import backend.knowhow.domain.driving.dto.response.PlaceSearchListResponse;
import backend.knowhow.domain.driving.dto.response.kakao.KakaoPlaceSearchResponse;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
        log.info("keyword: {}", keyword);
        // 카카오 키워드 기반 장소 검색 api 호출
        KakaoPlaceSearchResponse kakaoResponse = kakaoApiClient.searchByKeyword(keyword);

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
        log.info("memberId: {}", memberId);
        Member driver = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        DrivingSession drive = DrivingSession.start(driver, request.getLat(), request.getLon());
        DrivingSession saveDrive = drivingSessionRepository.save(drive);
        return new DriveStartResponse(saveDrive.getId(), saveDrive.getStartTime());
    }
}

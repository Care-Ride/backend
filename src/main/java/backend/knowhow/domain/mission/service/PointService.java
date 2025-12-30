package backend.knowhow.domain.mission.service;

import backend.knowhow.domain.member.domain.Member;

import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.domain.mission.domain.PointHistory;
import backend.knowhow.domain.mission.dto.PointBalanceResponse;
import backend.knowhow.domain.mission.dto.PointHistoryResponse;
import backend.knowhow.domain.mission.repository.PointHistoryRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@Service
@RequiredArgsConstructor
public class PointService {
    private final MemberRepository memberRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Transactional(readOnly = true)
    public Page<PointHistoryResponse> getMonthlyPointHistory(Long memberId, int page, int size, String yearMonth) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        if (page < 0 || size <= 0 || size > 50) {
            throw new BaseException(ErrorType.INVALID_PAGE_REQUEST);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        YearMonth month;
        if (yearMonth == null || yearMonth.isEmpty()) {
            month = YearMonth.now();
        } else {
            try {
                month = YearMonth.parse(yearMonth, YEAR_MONTH_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new BaseException(ErrorType.INVALID_DATE_FORMAT);
            }
        }
        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.plusMonths(1).atDay(1).atStartOfDay();    //다음달 1일 00:00

        return pointHistoryRepository
                .findAllByMemberAndCreatedAtBetween(member, start, end, pageable)
                .map(PointHistoryResponse::from);
    }


    @Transactional
    public void earnMissionReward(Member member, int rewardPoint, String description) {

        if (rewardPoint <= 0) {
            throw new BaseException(ErrorType.INVALID_POINT_AMOUNT);
        }
        member.addPoint(rewardPoint);
        pointHistoryRepository.save(
                PointHistory.earn(member, rewardPoint, member.getPointBalance(), description)
        );
    }

    @Transactional
    public void usePoint(Long memberId, int amount, String description) {

        if (amount <= 0) {
            throw new BaseException(ErrorType.INVALID_POINT_AMOUNT);
        }
        Member member = memberRepository.findByIdForUpdate(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));
        member.usePoint(amount);
        pointHistoryRepository.save(PointHistory.spend(member, amount, member.getPointBalance(), description));
    }

    @Transactional(readOnly = true)
    public PointBalanceResponse getBalance(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));
        return PointBalanceResponse.from(member.getPointBalance());
    }
}

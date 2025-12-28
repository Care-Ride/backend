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

@Service
@RequiredArgsConstructor
public class PointService {
    private final MemberRepository memberRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional(readOnly = true)
    public Page<PointHistoryResponse> getPointHistory(Long memberId, int page, int size) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending()
        );
        return pointHistoryRepository
                .findAllByMember(member, pageable)
                .map(PointHistoryResponse::from);
    }


    @Transactional
    public void earnMissionReward(Long memberId, int rewardPoint, String description) {

        if (rewardPoint <= 0) {
            throw new BaseException(ErrorType.INVALID_POINT_AMOUNT);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));
       member.addPoint(rewardPoint);
       pointHistoryRepository.save(PointHistory.earn(member, rewardPoint, member.getPointBalance(), description));
    }

    @Transactional
    public void usePoint(Long memberId, int amount, String description) {

        if (amount <= 0) {
            throw new BaseException(ErrorType.INVALID_POINT_AMOUNT);
        }
        Member member = memberRepository.findById(memberId)
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

package backend.knowhow.domain.member.service;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.dto.response.MemberInfoResponse;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        return new MemberInfoResponse(
                member.getId(),
                member.getNickname(),
                member.getRole()
        );
    }
}
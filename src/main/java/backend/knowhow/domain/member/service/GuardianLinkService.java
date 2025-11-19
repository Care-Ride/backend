package backend.knowhow.domain.member.service;

import backend.knowhow.domain.member.domain.GuardianLink;
import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.repository.GuardianLinkRepository;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuardianLinkService {

    private final GuardianLinkRepository guardianLinkRepository;
    private final MemberRepository memberRepository;

    public void link(Long guardianId, Long seniorId) {

        Member guardian = memberRepository.findById(guardianId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        Member senior = memberRepository.findById(seniorId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        if (guardianLinkRepository.existsByGuardianAndSenior(guardian, senior)) {
            throw new BaseException(ErrorType.ALREADY_LINKED);
        }
        GuardianLink link = new GuardianLink(guardian, senior);
        guardianLinkRepository.save(link);

    }
}

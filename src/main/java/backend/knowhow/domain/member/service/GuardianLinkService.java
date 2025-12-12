package backend.knowhow.domain.member.service;

import backend.knowhow.domain.member.domain.GuardianLink;
import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.domain.RelationType;
import backend.knowhow.domain.member.dto.response.GuardianViewLinkResponse;
import backend.knowhow.domain.member.dto.response.SeniorViewLinkResponse;
import backend.knowhow.domain.member.repository.GuardianLinkRepository;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Transactional
    public void updatePendingLink(
            Long guardianId,
            RelationType relationType,
            String customSeniorName
    ) {

        if (relationType == null) {
            throw new BaseException(ErrorType.LINK_INFO_INVALID);
        }

        if (relationType == RelationType.CUSTOM && !StringUtils.hasText(customSeniorName)) {
            throw new BaseException(ErrorType.LINK_INFO_INVALID);
        }

        if (relationType != RelationType.CUSTOM) {
            customSeniorName = null;
        }

        GuardianLink link = guardianLinkRepository
                .findByGuardianIdAndRelationTypeIsNull(guardianId)
                .orElseThrow(() -> new BaseException(ErrorType.LINK_NOT_FOUND));

        link.updateInfo(relationType, customSeniorName);
    }

    // 보호자 화면용 조회
    public GuardianViewLinkResponse getForGuardian(Long guardianId) {
        GuardianLink link = guardianLinkRepository
                .findByGuardianId(guardianId)
                .orElseThrow(() -> new BaseException(ErrorType.LINK_NOT_FOUND));

        String seniorName =
                link.getCustomSeniorName() != null
                        ? link.getCustomSeniorName()
                        : link.getSenior().getNickname();

        return new GuardianViewLinkResponse(
                seniorName,
                link.getRelationType()
        );
    }

    // 고령자 화면용 조회
    public SeniorViewLinkResponse getForSenior(Long seniorId) {
        GuardianLink link = guardianLinkRepository
                .findBySeniorId(seniorId)
                .orElseThrow(() -> new BaseException(ErrorType.LINK_NOT_FOUND));

        RelationType reversed = null;
        if (link.getRelationType() != null) {
            reversed = RelationMapper.reverse(link.getRelationType());
        }

        return new SeniorViewLinkResponse(
                link.getGuardian().getNickname(),
                reversed
        );
    }
}

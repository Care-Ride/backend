package backend.knowhow.domain.member.dto.response;

import backend.knowhow.domain.member.domain.RelationType;

public record GuardianViewLinkResponse(
        String seniorName,
        RelationType relationType
) {}

package backend.knowhow.domain.member.dto.request;

import backend.knowhow.domain.member.domain.RelationType;

public record LinkInfoRequest(
        RelationType relationType,
        String customSeniorName
) {}

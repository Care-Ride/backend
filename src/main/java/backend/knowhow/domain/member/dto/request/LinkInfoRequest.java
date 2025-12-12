package backend.knowhow.domain.member.dto.request;

import backend.knowhow.domain.member.domain.RelationType;
import jakarta.validation.constraints.NotNull;

public record LinkInfoRequest(
        @NotNull RelationType relationType,
        String customSeniorName
) {}

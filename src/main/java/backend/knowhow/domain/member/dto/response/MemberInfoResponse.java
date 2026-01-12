package backend.knowhow.domain.member.dto.response;

import backend.knowhow.domain.member.domain.Role;

public record MemberInfoResponse(
        Long id,
        String nickname,
        Role role
) {}
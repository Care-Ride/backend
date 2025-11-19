package backend.knowhow.domain.test.dto;

import backend.knowhow.domain.member.domain.Role;

public record TestMemberRequest(
        Long memberId,
        String nickname,
        Role role
) {}

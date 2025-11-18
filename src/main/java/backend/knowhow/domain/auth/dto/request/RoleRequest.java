package backend.knowhow.domain.auth.dto.request;

import backend.knowhow.domain.member.domain.Role;

public record RoleRequest(
        Role role
) {}

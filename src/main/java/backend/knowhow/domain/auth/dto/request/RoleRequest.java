package backend.knowhow.domain.auth.dto.request;

import backend.knowhow.domain.auth.domain.Role;

public record RoleRequest(
        Role role
) {}

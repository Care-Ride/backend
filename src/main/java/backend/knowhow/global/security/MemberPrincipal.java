package backend.knowhow.global.security;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.domain.Role;
import lombok.Getter;

import java.security.Principal;

@Getter
public class MemberPrincipal implements Principal {

    private final Long id;
    private final Role role;


    public MemberPrincipal(Member member) {
        this.id = member.getId();
        this.role = member.getRole();
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}

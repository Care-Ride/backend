package backend.knowhow.global.security;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.domain.Role;
import lombok.Getter;

@Getter
public class MemberPrincipal {

    private final Long id;
    private final Role role;


    public MemberPrincipal(Member member) {
        this.id = member.getId();
        this.role = member.getRole();
    }
}

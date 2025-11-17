package backend.knowhow.global.security;

import backend.knowhow.domain.auth.domain.Member;
import lombok.Getter;

@Getter
public class MemberPrincipal {

    private final Long id;


    public MemberPrincipal(Member member) {
        this.id = member.getId();
    }
}

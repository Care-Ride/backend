package backend.knowhow.domain.member.domain;

import backend.knowhow.domain.auth.dto.response.KakaoUserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long kakaoId;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(KakaoUserInfo info) {
        this.kakaoId = info.getId();
        this.nickname = info.getNickname();
        this.role = Role.NONE;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
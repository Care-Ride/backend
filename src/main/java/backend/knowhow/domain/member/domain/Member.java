package backend.knowhow.domain.member.domain;

import backend.knowhow.domain.auth.dto.response.KakaoUserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(KakaoUserInfo info) {
        this.kakaoId = info.getId();
        this.nickname = info.getNickname();
        this.role = Role.NONE;
    }

    // 테스트 계정 생성자
    public Member(String nickname, Role role) {
        this.nickname = nickname;
        this.role = role;
    }
}
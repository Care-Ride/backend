package backend.knowhow.domain.auth.domain;

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

    public Member(KakaoUserInfo info) {
        this.kakaoId = info.getId();
        this.nickname = info.getNickname();
    }
}
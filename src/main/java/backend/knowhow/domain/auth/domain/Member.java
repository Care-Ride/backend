package backend.knowhow.domain.auth.domain;

import backend.knowhow.domain.auth.dto.KakaoUserInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long kakaoId;
    private String nickname;

    public Member(KakaoUserInfo info) {
        this.kakaoId = info.getId();
        this.nickname = info.getNickname();
    }
}
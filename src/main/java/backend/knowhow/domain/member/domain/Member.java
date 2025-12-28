package backend.knowhow.domain.member.domain;

import backend.knowhow.domain.auth.domain.SocialType;
import backend.knowhow.domain.auth.dto.response.KakaoUserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable = false)
    private String socialId;

    private String nickname;

    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    // 구글 로그인 시 닉네임 없으면 임시 닉네임 생성
    private static String generateTempNickname() {
        return "USER_" + UUID.randomUUID().toString().substring(0, 8);
    }

    // 정적 팩토리 메서드

    public static Member createKakaoMember(KakaoUserInfo info) {
        Member member = new Member();
        member.socialType = SocialType.KAKAO;
        member.socialId = String.valueOf(info.id());
        member.nickname = info.nickname();
        member.role = Role.NONE;
        return member;
    }

    public static Member createGoogleMember(String googleSub, String nickname) {
        Member member = new Member();
        member.socialType = SocialType.GOOGLE;
        member.socialId = googleSub;
        member.nickname = (nickname == null || nickname.isBlank())
                ? generateTempNickname()
                : nickname;
        member.role = Role.NONE;
        return member;
    }

    public static Member createTestMember(String nickname, Role role) {
        Member member = new Member();
        member.socialType = SocialType.TEST;
        member.socialId = "TEST_" + UUID.randomUUID();
        member.nickname = nickname;
        member.role = role;
        return member;
    }
}
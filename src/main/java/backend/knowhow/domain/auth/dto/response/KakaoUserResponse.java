package backend.knowhow.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class KakaoUserResponse {

    private Long id;
    private KakaoAccount kakao_account;
    private Properties properties;

    @Getter
    public static class KakaoAccount {
        private String email;
    }

    @Getter
    public static class Properties {
        private String nickname;
        private String profile_image;
    }
}

package backend.knowhow.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class KakaoLoginRequest {
    private String accessToken;
}
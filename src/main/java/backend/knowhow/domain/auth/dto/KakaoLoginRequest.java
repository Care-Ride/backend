package backend.knowhow.domain.auth.dto;

import lombok.Getter;

@Getter
public class KakaoLoginRequest {
    private String accessToken;
}
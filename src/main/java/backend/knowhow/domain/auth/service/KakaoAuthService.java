package backend.knowhow.domain.auth.service;

import backend.knowhow.domain.auth.dto.response.KakaoUserInfo;
import backend.knowhow.domain.auth.dto.response.KakaoUserResponse;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    public KakaoUserInfo getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<KakaoUserResponse> response =
                restTemplate.exchange(
                        "https://kapi.kakao.com/v2/user/me",
                        HttpMethod.GET,
                        entity,
                        KakaoUserResponse.class
                );
        KakaoUserResponse body = response.getBody();
        if (body == null || body.getId() == null) {
            throw new BaseException(ErrorType.KAKAO_TOKEN_INVALID);
        }
        return new KakaoUserInfo(
                body.getId(),
                body.getProperties().getNickname()
        );
    }
}

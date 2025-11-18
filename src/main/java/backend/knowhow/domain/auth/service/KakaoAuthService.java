package backend.knowhow.domain.auth.service;

import backend.knowhow.domain.auth.dto.response.KakaoUserInfo;
import backend.knowhow.domain.auth.dto.response.KakaoUserResponse;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://kapi.kakao.com")
            .build();

    public KakaoUserInfo getUserInfo(String accessToken) {

        KakaoUserResponse response = webClient.get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new BaseException(ErrorType.KAKAO_TOKEN_INVALID)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new BaseException(ErrorType.EXTERNAL_API_ERROR)))
                .bodyToMono(KakaoUserResponse.class)
                .block();


        if (response == null || response.getId() == null) {
            throw new BaseException(ErrorType.KAKAO_TOKEN_INVALID);
        }
        if (response.getProperties() == null || response.getProperties().getNickname() == null) {
            throw new BaseException(ErrorType.KAKAO_TOKEN_INVALID);
        }

        return new KakaoUserInfo(
                response.getId(),
                response.getProperties().getNickname()
        );
    }
}

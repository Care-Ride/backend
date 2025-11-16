package backend.knowhow.domain.auth.service;

import backend.knowhow.domain.auth.dto.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoUserInfo getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map body = response.getBody();
        Long id = ((Number) body.get("id")).longValue();

        Map<String, Object> properties = (Map<String, Object>) body.get("properties");
        Map<String, Object> account = (Map<String, Object>) body.get("kakao_account");

        return new KakaoUserInfo(
                id,
                (String) properties.get("nickname")
        );
    }
}

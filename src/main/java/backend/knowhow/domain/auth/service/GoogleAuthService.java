package backend.knowhow.domain.auth.service;

import backend.knowhow.domain.auth.dto.response.GoogleUserInfo;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class GoogleAuthService {

    private final TokenVerifier tokenVerifier;

    public GoogleAuthService(
            @Value("${oauth.google.client-id}") String clientId
    ) {
        this.tokenVerifier = TokenVerifier.newBuilder()
                .setAudience(clientId)
                .build();
    }

    public GoogleUserInfo getUserInfo(String idToken) {

        JsonWebSignature jws;

        try {
            jws = tokenVerifier.verify(idToken);
        } catch (TokenVerifier.VerificationException e) {
            throw new BaseException(ErrorType.GOOGLE_TOKEN_INVALID);
        }

        if (jws == null || jws.getPayload() == null) {
            throw new BaseException(ErrorType.GOOGLE_TOKEN_INVALID);
        }

        JsonWebSignature.Payload payload = jws.getPayload();

        // Google user unique id (OIDC sub)
        String sub = payload.getSubject();

        if (sub == null) {
            throw new BaseException(ErrorType.GOOGLE_TOKEN_INVALID);
        }

        // name 은 optional
        Object nameObj = payload.get("name");
        String nickname = nameObj != null ? nameObj.toString() : null;

        return new GoogleUserInfo(sub, nickname);
    }
}

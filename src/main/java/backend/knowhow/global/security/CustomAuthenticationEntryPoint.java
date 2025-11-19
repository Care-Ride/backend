package backend.knowhow.global.security;

import backend.knowhow.global.common.response.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        ErrorType errorType = ErrorType.TOKEN_REQUIRED;

        response.setStatus(errorType.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");

        String json = String.format(
                "{ \"status\": %d, \"code\": \"%s\", \"message\": \"%s\", \"data\": null }",
                errorType.getHttpStatus().value(),
                errorType.getCode(),
                errorType.getMessage()
        );

        response.getWriter().write(json);
    }
}

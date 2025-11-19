package backend.knowhow.global.security;

import backend.knowhow.global.common.response.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {

        ErrorType errorType = ErrorType.ACCESS_DENIED;

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

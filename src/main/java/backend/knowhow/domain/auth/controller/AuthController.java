package backend.knowhow.domain.auth.controller;

import backend.knowhow.domain.auth.dto.response.AuthResponse;
import backend.knowhow.domain.auth.dto.request.KakaoLoginRequest;
import backend.knowhow.domain.auth.dto.request.ReissueRequest;
import backend.knowhow.domain.auth.service.AuthService;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao")
    public ApiResponse<?> loginKakao(@RequestBody KakaoLoginRequest request) {

        AuthResponse response = authService.loginKakao(request.getAccessToken());

        return ApiResponse.success(response);
    }

    @PostMapping("/refresh")
    public ApiResponse<?> refresh(@RequestBody ReissueRequest request) {
        String accessToken = authService.refresh(request.refreshToken());
        return ApiResponse.success(new AuthResponse(accessToken));
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(@CurrentUser MemberPrincipal member) {
        log.info("Logout API reached");
        authService.logout(member.getId());
        log.info("Logout SUCCESS");
        return ApiResponse.success(null);
    }


}

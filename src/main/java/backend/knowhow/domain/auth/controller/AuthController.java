package backend.knowhow.domain.auth.controller;

import backend.knowhow.domain.auth.dto.response.AuthResponse;
import backend.knowhow.domain.auth.dto.request.KakaoLoginRequest;
import backend.knowhow.domain.auth.dto.request.ReissueRequest;
import backend.knowhow.domain.auth.service.AuthService;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao")
    public ApiResponse<AuthResponse> loginKakao(@RequestBody KakaoLoginRequest request) {

        AuthResponse response = authService.loginKakao(request.getAccessToken());

        return ApiResponse.success(response);
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@RequestBody ReissueRequest request) {
        String accessToken = authService.refresh(request.refreshToken());
        return ApiResponse.success(new AuthResponse(accessToken, request.refreshToken()));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@CurrentUser MemberPrincipal member) {
        authService.logout(member.getId());
        return ApiResponse.success(null);
    }


}

package backend.knowhow.domain.auth.controller;

import backend.knowhow.domain.auth.dto.AuthResponse;
import backend.knowhow.domain.auth.dto.KakaoLoginRequest;
import backend.knowhow.domain.auth.service.AuthService;
import backend.knowhow.global.common.response.ApiResponse;
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
    public ApiResponse<?> kakaoLogin(@RequestBody KakaoLoginRequest request) {

        AuthResponse response = authService.kakaoLogin(request.getAccessToken());

        return ApiResponse.success(response);
    }


}

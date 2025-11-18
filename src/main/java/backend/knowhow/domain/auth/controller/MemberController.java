package backend.knowhow.domain.auth.controller;

import backend.knowhow.domain.auth.domain.Member;
import backend.knowhow.domain.auth.dto.request.RoleRequest;
import backend.knowhow.domain.auth.dto.response.AuthResponse;
import backend.knowhow.domain.auth.dto.response.MemberInfoResponse;
import backend.knowhow.domain.auth.repository.MemberRepository;
import backend.knowhow.domain.auth.service.AuthService;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.common.response.ErrorType;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberRepository memberRepository;
    private final AuthService authService;

    @GetMapping("/me")
    public ApiResponse<MemberInfoResponse> getMyInfo(@CurrentUser MemberPrincipal user) {

        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        return ApiResponse.success(
                new MemberInfoResponse(
                        member.getId(),
                        member.getNickname()
                )
        );
    }

    @PostMapping("/role")
    public ApiResponse<AuthResponse> selectRole(@CurrentUser MemberPrincipal user, @RequestBody RoleRequest request
    ) {
        AuthResponse response = authService.selectRole(user.getId(), request.role());
        return ApiResponse.success(response);
    }

}


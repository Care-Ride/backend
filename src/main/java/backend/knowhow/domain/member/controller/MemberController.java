package backend.knowhow.domain.member.controller;

import backend.knowhow.domain.auth.dto.request.RoleRequest;
import backend.knowhow.domain.auth.dto.response.AuthResponse;
import backend.knowhow.domain.member.dto.response.MemberInfoResponse;
import backend.knowhow.domain.member.service.MemberService;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ApiResponse<MemberInfoResponse> getMyInfo(@CurrentUser MemberPrincipal user) {

        MemberInfoResponse response = memberService.getMyInfo(user.getId());
        return ApiResponse.success(response);
    }



}


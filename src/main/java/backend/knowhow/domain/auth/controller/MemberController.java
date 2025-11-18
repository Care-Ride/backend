package backend.knowhow.domain.auth.controller;

import backend.knowhow.domain.auth.domain.Member;
import backend.knowhow.domain.auth.dto.response.MemberInfoResponse;
import backend.knowhow.domain.auth.repository.MemberRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.common.response.ErrorType;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberRepository memberRepository;

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
}


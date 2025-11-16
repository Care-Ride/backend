package backend.knowhow.domain.auth.controller;

import backend.knowhow.domain.auth.domain.Member;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.common.response.ErrorType;
import backend.knowhow.global.security.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final UserUtil userUtil;

    @GetMapping("/me")
    public ApiResponse<?> getMyInfo() {

        Member member = userUtil.getCurrentMember();

        if (member == null) {
            return ApiResponse.error(ErrorType.MEMBER_NOT_FOUND);
        }

        return ApiResponse.success(
                new MemberInfoResponse(
                        member.getId(),
                        member.getKakaoId(),
                        member.getNickname()
                )
        );
    }

    private record MemberInfoResponse(
            Long id,
            Long kakaoId,
            String nickname
    ) {}
}


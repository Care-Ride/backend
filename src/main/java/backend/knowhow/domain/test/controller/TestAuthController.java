package backend.knowhow.domain.test.controller;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.domain.test.dto.TestLoginRequest;
import backend.knowhow.domain.test.dto.TestMemberRequest;
import backend.knowhow.domain.test.dto.TestMemberResponse;
import backend.knowhow.domain.test.dto.TokenResponse;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.common.response.ErrorType;
import backend.knowhow.global.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/test")
@RequiredArgsConstructor
public class TestAuthController {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ApiResponse<TestMemberResponse> createTestMember(
            @RequestBody TestMemberRequest request
    ) {
        Member member = new Member(request.nickname(), request.role());
        memberRepository.save(member);

        return ApiResponse.success(new TestMemberResponse(member.getId()));
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponse> testLogin(
            @RequestBody TestLoginRequest request
    ) {
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        String token = jwtUtil.createAccessToken(member.getId(), member.getRole());

        return ApiResponse.success(new TokenResponse(token));
    }
}
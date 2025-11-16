package backend.knowhow.domain.auth.service;

import backend.knowhow.domain.auth.domain.Member;
import backend.knowhow.domain.auth.dto.AuthResponse;
import backend.knowhow.domain.auth.dto.KakaoUserInfo;
import backend.knowhow.domain.auth.repository.MemberRepository;
import backend.knowhow.global.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final KakaoAuthService kakaoAuthService;
    private final JwtUtil jwtUtil;

    public AuthResponse kakaoLogin(String accessToken) {

        KakaoUserInfo userInfo = kakaoAuthService.getUserInfo(accessToken);

        Member member = memberRepository.findByKakaoId(userInfo.getId())
                .orElseGet(() -> memberRepository.save(new Member(userInfo)));

        String jwt = jwtUtil.generateToken(member.getId());

        return new AuthResponse(
                member.getId(),
                member.getNickname(),
                jwt
        );
    }
}
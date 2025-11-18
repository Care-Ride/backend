package backend.knowhow.domain.auth.service;

import backend.knowhow.domain.auth.domain.Member;
import backend.knowhow.domain.auth.dto.response.AuthResponse;
import backend.knowhow.domain.auth.dto.response.KakaoUserInfo;
import backend.knowhow.domain.auth.repository.MemberRepository;
import backend.knowhow.domain.auth.repository.RefreshTokenRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import backend.knowhow.global.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KakaoAuthService kakaoAuthService;
    private final JwtUtil jwtUtil;

    public AuthResponse loginKakao(String accessToken) {

        KakaoUserInfo userInfo = kakaoAuthService.getUserInfo(accessToken);

        Member member = memberRepository.findByKakaoId(userInfo.getId())
                .orElseGet(() -> memberRepository.save(new Member(userInfo)));

        // accessToken 발급
        String access = jwtUtil.createAccessToken(member.getId());

        // refreshToken 발급
        String refresh = jwtUtil.createRefreshToken(member.getId());
        refreshTokenRepository.save(member.getId(), refresh, 14);

        return new AuthResponse(access, refresh);
    }

    public String refresh(String refreshToken) {

        Long memberId = jwtUtil.validateAndExtractMemberId(refreshToken);

        String savedToken = refreshTokenRepository.find(memberId);

        if (savedToken == null) {
            throw new BaseException(ErrorType.REFRESH_TOKEN_NOT_FOUND);
        }

        if (!savedToken.equals(refreshToken)) {
            throw new BaseException(ErrorType.INVALID_REFRESH_TOKEN);
        }

        return jwtUtil.createAccessToken(memberId);
    }

    public void logout(Long memberId) {
        refreshTokenRepository.delete(memberId);
    }
}
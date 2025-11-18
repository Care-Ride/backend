package backend.knowhow.domain.auth.service;

import backend.knowhow.domain.auth.domain.Member;
import backend.knowhow.domain.auth.domain.Role;
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
        String access = jwtUtil.createAccessToken(member.getId(), member.getRole());

        // refreshToken 발급
        String refresh = jwtUtil.createRefreshToken(member.getId());
        refreshTokenRepository.save(member.getId(), refresh);

        return new AuthResponse(access, refresh);
    }

    public AuthResponse selectRole(Long memberId, Role role) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        member.setRole(role);
        memberRepository.save(member);

        String newAccess = jwtUtil.createAccessToken(member.getId(), member.getRole());
        String newRefresh = jwtUtil.createRefreshToken(member.getId());
        refreshTokenRepository.save(member.getId(), newRefresh);
        return new AuthResponse(newAccess, newRefresh);
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        return jwtUtil.createAccessToken(memberId, member.getRole());
    }

    public void logout(Long memberId) {
        refreshTokenRepository.delete(memberId);
    }
}
package backend.knowhow.domain.auth.service;

import backend.knowhow.domain.auth.dto.response.LoginResponse;
import backend.knowhow.domain.driving.repository.DrivingSessionRepository;
import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.domain.Role;
import backend.knowhow.domain.auth.dto.response.AuthResponse;
import backend.knowhow.domain.auth.dto.response.KakaoUserInfo;
import backend.knowhow.domain.member.repository.GuardianLinkRepository;
import backend.knowhow.domain.member.repository.MemberDeviceSettingRepository;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.domain.auth.repository.RefreshTokenRepository;
import backend.knowhow.domain.member.service.MemberDeviceSettingService;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import backend.knowhow.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberDeviceSettingService deviceSettingService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final GuardianLinkRepository guardianLinkRepository;
    private final DrivingSessionRepository drivingSessionRepository;
    private final MemberDeviceSettingRepository memberDeviceSettingRepository;
    private final KakaoAuthService kakaoAuthService;
    private final JwtUtil jwtUtil;

    public LoginResponse loginKakao(String accessToken) {

        KakaoUserInfo userInfo = kakaoAuthService.getUserInfo(accessToken);

        Member member = memberRepository.findByKakaoId(userInfo.getId())
                .orElseGet(() -> memberRepository.save(new Member(userInfo)));

        // accessToken 발급
        String access = jwtUtil.createAccessToken(member.getId(), member.getRole());

        // refreshToken 발급
        String refresh = jwtUtil.createRefreshToken(member.getId());
        refreshTokenRepository.save(member.getId(), refresh);

        // 화면 세팅 여부
        Boolean hasDeviceSetting = deviceSettingService.existsSettingByMember(member);

        return new LoginResponse(access, refresh, hasDeviceSetting);
    }

    public AuthResponse selectRole(Long memberId, Role role) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        if (role == Role.ADMIN) {
            throw new BaseException(ErrorType.INVALID_ROLE);
        }

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

    @Transactional
    public void withdraw(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

        refreshTokenRepository.delete(memberId);
        guardianLinkRepository.deleteByGuardianIdOrSeniorId(memberId);
        drivingSessionRepository.deleteByDriver_Id(memberId);
        memberDeviceSettingRepository.deleteByMemberId(memberId);
        memberRepository.delete(member);
    }
}
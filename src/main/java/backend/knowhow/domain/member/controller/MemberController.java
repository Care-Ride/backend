package backend.knowhow.domain.member.controller;

import backend.knowhow.domain.member.dto.request.ConnectRequest;
import backend.knowhow.domain.member.dto.request.DeviceSettingRequest;
import backend.knowhow.domain.member.dto.request.LinkInfoRequest;
import backend.knowhow.domain.member.dto.response.ConnectionCodeResponse;
import backend.knowhow.domain.member.dto.response.DeviceSettingResponse;
import backend.knowhow.domain.member.dto.response.GuardianViewLinkResponse;
import backend.knowhow.domain.member.dto.response.MemberInfoResponse;
import backend.knowhow.domain.member.dto.response.SeniorViewLinkResponse;
import backend.knowhow.domain.member.service.ConnectionCodeService;
import backend.knowhow.domain.member.service.GuardianLinkService;
import backend.knowhow.domain.member.service.MemberDeviceSettingService;
import backend.knowhow.domain.member.service.MemberService;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final ConnectionCodeService connectionCodeService;
    private final GuardianLinkService guardianLinkService;
    private final MemberDeviceSettingService settingService;

    @GetMapping("/me")
    public ApiResponse<MemberInfoResponse> getMyInfo(@CurrentUser MemberPrincipal user) {

        MemberInfoResponse response = memberService.getMyInfo(user.getId());
        return ApiResponse.success(response);
    }

    @PreAuthorize("hasRole('SENIOR')")
    @PostMapping("/link/generate")
    public ApiResponse<ConnectionCodeResponse> generate(@CurrentUser MemberPrincipal user) {
        Long seniorId = user.getId();
        String code = connectionCodeService.generateCode(seniorId);

        return ApiResponse.success(new ConnectionCodeResponse(code, connectionCodeService.getExpireSeconds()));
    }

    @PreAuthorize("hasRole('GUARDIAN')")
    @PostMapping("/link/connect")
    public ApiResponse<Void> connect(
            @CurrentUser MemberPrincipal guardian,
            @RequestBody ConnectRequest request
    ) {
        Long seniorId = connectionCodeService.verifyCode(request.code());
        guardianLinkService.link(guardian.getId(), seniorId);
        connectionCodeService.deleteCode(request.code());

        return ApiResponse.success();
    }

    @PostMapping("/device-setting")
    public DeviceSettingResponse saveOrUpdateSetting(
            @CurrentUser MemberPrincipal member,
            @Valid @RequestBody DeviceSettingRequest request
    ) {
        return settingService.saveOrUpdateSetting(member.getId(), request);
    }

    @GetMapping("/device-setting")
    public DeviceSettingResponse getSetting(
            @CurrentUser MemberPrincipal member
    ) {
        return settingService.getSetting(member.getId());
    }

    @PreAuthorize("hasRole('GUARDIAN')")
    @PatchMapping("/link/info")
    public ApiResponse<Void> updateMyLinkInfo(
            @CurrentUser MemberPrincipal guardian,
            @Valid @RequestBody LinkInfoRequest request
    ) {
        guardianLinkService.updatePendingLink(
                guardian.getId(),
                request.relationType(),
                request.customSeniorName()
        );
        return ApiResponse.success();
    }

    // 보호자 화면
    @PreAuthorize("hasRole('GUARDIAN')")
    @GetMapping("/link/senior")
    public ApiResponse<GuardianViewLinkResponse> getMySenior(
            @CurrentUser MemberPrincipal guardian
    ) {
        return ApiResponse.success(
                guardianLinkService.getForGuardian(guardian.getId())
        );
    }

    // 고령자 화면
    @PreAuthorize("hasRole('SENIOR')")
    @GetMapping("/link/guardian")
    public ApiResponse<SeniorViewLinkResponse> getMyGuardian(
            @CurrentUser MemberPrincipal senior
    ) {
        return ApiResponse.success(
                guardianLinkService.getForSenior(senior.getId())
        );
    }

    // 연동 해제하기
    @PreAuthorize("hasAnyRole('GUARDIAN', 'SENIOR')")
    @DeleteMapping("/link")
    public ApiResponse<Void> unlink(@CurrentUser MemberPrincipal member) {
        guardianLinkService.unlink(member.getId());
        return ApiResponse.success();
    }

}
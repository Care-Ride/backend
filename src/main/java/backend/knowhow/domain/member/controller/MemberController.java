package backend.knowhow.domain.member.controller;

import backend.knowhow.domain.member.dto.request.ConnectRequest;
import backend.knowhow.domain.member.dto.request.DeviceSettingRequest;
import backend.knowhow.domain.member.dto.response.ConnectionCodeResponse;
import backend.knowhow.domain.member.dto.response.DeviceSettingResponse;
import backend.knowhow.domain.member.dto.response.MemberInfoResponse;
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

    @GetMapping("/device-setting/{deviceId}")
    public DeviceSettingResponse getSetting(
            @CurrentUser MemberPrincipal member,
            @PathVariable String deviceId
    ) {
        return settingService.getSetting(member.getId(), deviceId);
    }

}
package backend.knowhow.domain.vehicle.controller;

import backend.knowhow.domain.vehicle.dto.VehicleCreateRequest;
import backend.knowhow.domain.vehicle.dto.VehicleResponse;
import backend.knowhow.domain.vehicle.dto.VehicleUpdateRequest;
import backend.knowhow.domain.vehicle.service.VehicleService;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ApiResponse<VehicleResponse> register(
            @CurrentUser MemberPrincipal memberPrincipal,
            @RequestBody VehicleCreateRequest request
    ) {
        return ApiResponse.success(
                vehicleService.registerVehicle(memberPrincipal.getId(), request)
        );
    }

    @GetMapping
    public ApiResponse<List<VehicleResponse>> list(
            @CurrentUser MemberPrincipal memberPrincipal
    ) {
        return ApiResponse.success(
                vehicleService.getMyVehicles(memberPrincipal.getId())
        );
    }

    // 선택 차량 변경
    @PatchMapping("/{vehicleId}/activate")
    public ApiResponse<Void> activate(
            @CurrentUser MemberPrincipal memberPrincipal,
            @PathVariable Long vehicleId
    ) {
        vehicleService.changeActiveVehicle(memberPrincipal.getId(), vehicleId);
        return ApiResponse.success();
    }

    // 차량 정보 수정
    @PatchMapping("/{vehicleId}")
    public ApiResponse<VehicleResponse> updateInfo(
            @CurrentUser MemberPrincipal memberPrincipal,
            @PathVariable Long vehicleId,
            @RequestBody VehicleUpdateRequest request
    ) {
        return ApiResponse.success(
                vehicleService.updateVehicle(
                        memberPrincipal.getId(),
                        vehicleId,
                        request
                )
        );
    }
}

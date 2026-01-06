package backend.knowhow.domain.vehicle.controller;

import backend.knowhow.domain.vehicle.dto.request.BleBindRequest;
import backend.knowhow.domain.vehicle.dto.request.VehicleCreateRequest;
import backend.knowhow.domain.vehicle.dto.response.VehicleResponse;
import backend.knowhow.domain.vehicle.dto.request.VehicleUpdateRequest;
import backend.knowhow.domain.vehicle.service.VehicleService;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ApiResponse<VehicleResponse> register(
            @CurrentUser MemberPrincipal memberPrincipal,
            @Valid @RequestBody VehicleCreateRequest request
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
            @PathVariable @Positive Long vehicleId
    ) {
        vehicleService.changeActiveVehicle(memberPrincipal.getId(), vehicleId);
        return ApiResponse.success();
    }

    // 차량 정보 수정
    @PatchMapping("/{vehicleId}")
    public ApiResponse<VehicleResponse> updateInfo(
            @CurrentUser MemberPrincipal memberPrincipal,
            @PathVariable @Positive Long vehicleId,
            @Valid @RequestBody VehicleUpdateRequest request
    ) {
        return ApiResponse.success(
                vehicleService.updateVehicle(
                        memberPrincipal.getId(),
                        vehicleId,
                        request
                )
        );
    }

    // BLE 연동
    @PatchMapping("/{vehicleId}/ble")
    public ApiResponse<Void> bindBle(
            @CurrentUser MemberPrincipal memberPrincipal,
            @PathVariable @Positive Long vehicleId,
            @Valid @RequestBody BleBindRequest request
    ) {
        vehicleService.bindBleDevice(
                memberPrincipal.getId(),
                vehicleId,
                request.getBleDeviceId()
        );
        return ApiResponse.success();
    }

    // BLE 연동 해제
    @DeleteMapping("/{vehicleId}/ble")
    public ApiResponse<Void> unbindBle(
            @CurrentUser MemberPrincipal memberPrincipal,
            @PathVariable @Positive Long vehicleId
    ) {
        vehicleService.unbindBleDevice(
                memberPrincipal.getId(),
                vehicleId
        );
        return ApiResponse.success();
    }


}

package backend.knowhow.domain.driving.controller;

import backend.knowhow.domain.driving.dto.request.DriveEndRequest;
import backend.knowhow.domain.driving.dto.request.LocationRequest;
import backend.knowhow.domain.driving.dto.response.BeforeDriveDangerResponse;
import backend.knowhow.domain.driving.dto.response.DriveStartResponse;
import backend.knowhow.domain.driving.dto.response.PlaceSearchListResponse;
import backend.knowhow.domain.driving.dto.summary.DrivingSessionSummary;
import backend.knowhow.domain.driving.service.DrivingService;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drive")
public class DrivingController {

    private final DrivingService drivingService;

    @PostMapping("/danger")
    public ApiResponse<BeforeDriveDangerResponse> getDanger(@RequestBody LocationRequest request){
        BeforeDriveDangerResponse response = drivingService.getDangerBeforeDrive(request);
        return ApiResponse.success(response);
    }

    @GetMapping("/dst/search/{keyword}")
    public ApiResponse<PlaceSearchListResponse> searchDst(@PathVariable String keyword){
        PlaceSearchListResponse response = drivingService.searchDst(keyword);
        return ApiResponse.success(response);
    }

    @PostMapping("/start")
    public ApiResponse<DriveStartResponse> startDriving(@RequestBody LocationRequest locationRequest, @CurrentUser MemberPrincipal user){
        DriveStartResponse response = drivingService.startDriving(user.getId(), locationRequest);
        return ApiResponse.success(response);
    }

    @PostMapping("/end")
    public ApiResponse<DrivingSessionSummary> endDriving(@RequestBody DriveEndRequest request, @CurrentUser MemberPrincipal user){
        DrivingSessionSummary response = drivingService.endDriving(user.getId(), request);
        return ApiResponse.success(response);
    }
}

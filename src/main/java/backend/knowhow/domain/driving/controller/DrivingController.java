package backend.knowhow.domain.driving.controller;

import backend.knowhow.domain.driving.dto.request.DriveEndRequest;
import backend.knowhow.domain.driving.dto.request.LocationRequest;
import backend.knowhow.domain.driving.dto.response.BeforeDriveDangerResponse;
import backend.knowhow.domain.driving.dto.response.DailyDrivingListResponse;
import backend.knowhow.domain.driving.dto.response.DriveStartResponse;
import backend.knowhow.domain.driving.dto.response.PlaceSearchListResponse;
import backend.knowhow.domain.driving.dto.summary.DrivingSessionSummary;
import backend.knowhow.domain.driving.service.DrivingService;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drive")
public class DrivingController {

    private final DrivingService drivingService;

    // 운전 시작 전 위험요소 조회
    @PostMapping("/danger")
    public ApiResponse<BeforeDriveDangerResponse> getDanger(@RequestBody LocationRequest request){
        BeforeDriveDangerResponse response = drivingService.getDangerBeforeDrive(request);
        return ApiResponse.success(response);
    }

    // 목적지 검색
    @GetMapping("/dst/search/{keyword}")
    public ApiResponse<PlaceSearchListResponse> searchDst(@PathVariable String keyword){
        PlaceSearchListResponse response = drivingService.searchDst(keyword);
        return ApiResponse.success(response);
    }

    // 운전 시작
    @PostMapping("/start")
    public ApiResponse<DriveStartResponse> startDriving(
            @RequestBody LocationRequest locationRequest,
            @CurrentUser MemberPrincipal user
    ){
        DriveStartResponse response = drivingService.startDriving(user.getId(), locationRequest);
        return ApiResponse.success(response);
    }

    // 운전 종료
    @PostMapping("/end")
    public ApiResponse<DrivingSessionSummary> endDriving(
            @RequestBody DriveEndRequest request,
            @CurrentUser MemberPrincipal user
    ){
        DrivingSessionSummary response = drivingService.endDriving(user.getId(), request);
        return ApiResponse.success(response);
    }

    // 일별 운전 기록 조회
    @GetMapping("/daily/{date}")
    public ApiResponse<DailyDrivingListResponse> getDailyDrivingRecords(
            @PathVariable LocalDate date,
            @CurrentUser MemberPrincipal user
    ){
        DailyDrivingListResponse response = drivingService.getDailyDrivingRecords(date, user.getId());
        return ApiResponse.success(response);
    }
}

package backend.knowhow.domain.driving.controller;

import backend.knowhow.domain.driving.dto.request.BeforeDriveDangerRequest;
import backend.knowhow.domain.driving.dto.response.BeforeDriveDangerResponse;
import backend.knowhow.domain.driving.service.DrivingService;
import backend.knowhow.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drive")
public class DrivingController {

    private final DrivingService drivingService;

    @PostMapping("/danger")
    public ApiResponse<BeforeDriveDangerResponse> getDanger(@RequestBody BeforeDriveDangerRequest request){
        BeforeDriveDangerResponse response = drivingService.getDangerBeforeDrive(request);
        return ApiResponse.success(response);
    }
}

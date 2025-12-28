package backend.knowhow.domain.mobility.controller;

import backend.knowhow.domain.mobility.dto.MobilityRequest;
import backend.knowhow.domain.mobility.dto.MobilityResponse;
import backend.knowhow.domain.mobility.service.MobilityService;
import backend.knowhow.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mobility")
public class MobilityController {

    private final MobilityService mobilityService;

    @PostMapping("/call")
    public ApiResponse<MobilityResponse> call(@Valid @RequestBody MobilityRequest request) {

        String phoneNumber = mobilityService.getPhoneNumber(request.lat(), request.lon(), request.mobilityType());
        return ApiResponse.success(new MobilityResponse(phoneNumber));
    }
}

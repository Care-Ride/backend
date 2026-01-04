package backend.knowhow.domain.gifticon.controller;

import backend.knowhow.domain.gifticon.dto.response.GifticonProductListResponse;
import backend.knowhow.domain.gifticon.service.GifticonService;
import backend.knowhow.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gifticon")
public class GifticonController {

    private final GifticonService gifticonService;

    @GetMapping
    public ApiResponse<GifticonProductListResponse> getGifticonList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam int size
    ){
        GifticonProductListResponse response = gifticonService.getAvailableProducts(page, size);
        return ApiResponse.success(response);
    }
}

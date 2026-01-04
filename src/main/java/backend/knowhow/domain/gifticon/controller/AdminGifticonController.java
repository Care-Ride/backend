package backend.knowhow.domain.gifticon.controller;

import backend.knowhow.domain.gifticon.dto.admin.request.GifticonProductCreateRequest;
import backend.knowhow.domain.gifticon.dto.summary.GifticonProductSummary;
import backend.knowhow.domain.gifticon.service.AdminGifticonService;
import backend.knowhow.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/gifticon")
public class AdminGifticonController {

    private final AdminGifticonService gifticonService;

    @GetMapping
    public ApiResponse<List<GifticonProductSummary>> getGifticonList(){
        List<GifticonProductSummary> response = gifticonService.getAllProducts();
        return ApiResponse.success(response);
    }

    @PostMapping
    public ApiResponse<GifticonProductSummary> addGifticonProduct(
            @RequestBody GifticonProductCreateRequest request
    ){
        GifticonProductSummary response = gifticonService.addGifticonProduct(request);
        return ApiResponse.success(response);
    }
}

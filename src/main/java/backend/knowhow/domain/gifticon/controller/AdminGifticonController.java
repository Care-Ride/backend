package backend.knowhow.domain.gifticon.controller;

import backend.knowhow.domain.gifticon.dto.admin.request.GifticonBarcodeInsertRequest;
import backend.knowhow.domain.gifticon.dto.admin.request.GifticonProductCreateRequest;
import backend.knowhow.domain.gifticon.dto.summary.GifticonBarcodeSummary;
import backend.knowhow.domain.gifticon.dto.summary.GifticonProductSummary;
import backend.knowhow.domain.gifticon.service.AdminGifticonService;
import backend.knowhow.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // 상품 이미지 & 상품 정보 함께 전달받아서 저장
    @PostMapping(
            value = "product",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<GifticonProductSummary> addGifticonProduct(
            @RequestPart("request") GifticonProductCreateRequest request,
            @RequestPart("image") MultipartFile image
    ){
        GifticonProductSummary response = gifticonService.addGifticonProduct(request, image);
        return ApiResponse.success(response);
    }

    // 기프티콘 이미지 & 상품 id 저장
    @PostMapping(
            value = "/barcode",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<GifticonBarcodeSummary> addGifticon(
            @RequestPart("request") GifticonBarcodeInsertRequest request,
            @RequestPart("image") MultipartFile image
    ){
        GifticonBarcodeSummary response = gifticonService.addGifticonBarcodeImage(request, image);
        return ApiResponse.success(response);
    }
}

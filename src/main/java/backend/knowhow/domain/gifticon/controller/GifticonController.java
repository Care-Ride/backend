package backend.knowhow.domain.gifticon.controller;

import backend.knowhow.domain.gifticon.dto.summary.GifticonBarcodeSummary;
import backend.knowhow.domain.gifticon.dto.user.request.GifticonBarcodeRequest;
import backend.knowhow.domain.gifticon.dto.user.request.GifticonPurchaseRequest;
import backend.knowhow.domain.gifticon.dto.user.response.GifticonHistoryListResponse;
import backend.knowhow.domain.gifticon.dto.user.response.GifticonProductListResponse;
import backend.knowhow.domain.gifticon.service.GifticonService;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.security.CurrentUser;
import backend.knowhow.global.security.MemberPrincipal;
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
            @RequestParam int size,
            @CurrentUser MemberPrincipal user
    ){
        GifticonProductListResponse response = gifticonService.getAvailableProducts(page, size, user.getId());
        return ApiResponse.success(response);
    }

    @PostMapping("/purchase")
    public ApiResponse<GifticonBarcodeSummary> purchaseGifticon(
            @RequestBody GifticonPurchaseRequest request,
            @CurrentUser MemberPrincipal user
    ){
        GifticonBarcodeSummary response = gifticonService.purchaseGifticon(request, user.getId());
        return ApiResponse.success(response);
    }

    @GetMapping("/history")
    public ApiResponse<GifticonHistoryListResponse> getGifticonHistoryList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam int size,
            @CurrentUser MemberPrincipal user
    ){
        GifticonHistoryListResponse response = gifticonService.getGifticonHistoryList(page, size, user.getId());
        return ApiResponse.success(response);
    }

    @PostMapping("/barcode")
    public ApiResponse<GifticonBarcodeSummary> getGifticonHistoryList(
            @RequestBody GifticonBarcodeRequest request,
            @CurrentUser MemberPrincipal user
    ){
        GifticonBarcodeSummary response = gifticonService.getGifticonBarcode(request, user.getId());
        return ApiResponse.success(response);
    }
}

package backend.knowhow.domain.gifticon.dto.summary;

import backend.knowhow.domain.gifticon.domain.Gifticon;
import backend.knowhow.domain.gifticon.domain.GifticonProduct;
import backend.knowhow.domain.gifticon.domain.GifticonUsage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GifticonHistorySummary {
    private Long barcodeId;
    private String productImageUrl;
    private String brandName;
    private String productName;

    public static GifticonHistorySummary of(GifticonProduct product, Gifticon gifticon, String barcodeImageUrl){
        return GifticonHistorySummary.builder()
                .barcodeId(gifticon.getId())
                .productImageUrl(barcodeImageUrl)
                .brandName(product.getBrandName())
                .productName(product.getProductName())
                .build();
    }
}

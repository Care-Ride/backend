package backend.knowhow.domain.gifticon.dto.summary;

import backend.knowhow.domain.gifticon.domain.GifticonProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GifticonProductSummary {
    private Long id;
    private String imageUrl;
    private String brandName;
    private String productName;
    private int requiredPoint;
    private int stock;

    public static GifticonProductSummary of(GifticonProduct product, String presignedImageUrl){
        return GifticonProductSummary.builder()
                .id(product.getId())
                .imageUrl(presignedImageUrl)
                .brandName(product.getBrandName())
                .productName(product.getProductName())
                .requiredPoint(product.getRequiredPoint())
                .stock(product.getStock())
                .build();
    }

    public static GifticonProductSummary from(GifticonProduct product){
        return GifticonProductSummary.builder()
                .id(product.getId())
                .imageUrl(product.getImageKey())
                .brandName(product.getBrandName())
                .productName(product.getProductName())
                .requiredPoint(product.getRequiredPoint())
                .stock(product.getStock())
                .build();
    }
}

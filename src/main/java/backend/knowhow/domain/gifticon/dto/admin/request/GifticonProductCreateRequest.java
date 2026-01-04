package backend.knowhow.domain.gifticon.dto.admin.request;

import backend.knowhow.domain.gifticon.domain.GifticonProduct;
import lombok.Getter;

@Getter
public class GifticonProductCreateRequest {
    private String imageUrl;
    private String brandName;
    private String productName;
    private int requiredPoint;

    public GifticonProduct toEntity() {
        return GifticonProduct.builder()
                .imageUrl(imageUrl)
                .brandName(brandName)
                .productName(productName)
                .requiredPoint(requiredPoint)
                .stock(0)   // 재고 입력 전 상품 등록이기 때문에, 기본값 0
                .build();
    }
}

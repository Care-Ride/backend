package backend.knowhow.domain.gifticon.dto.admin.request;

import backend.knowhow.domain.gifticon.domain.Gifticon;
import backend.knowhow.domain.gifticon.domain.GifticonProduct;
import lombok.Getter;

@Getter
public class GifticonBarcodeInsertRequest {
    private Long productId;

    public Gifticon toEntity(String imageKey, GifticonProduct product) {
        return Gifticon.builder()
                .product(product)
                .imageKey(imageKey)
                .build();
    }
}

package backend.knowhow.domain.gifticon.dto.summary;

import backend.knowhow.domain.gifticon.domain.Gifticon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GifticonBarcodeSummary {
    private Long id;
    private String imageUrl;
    private String productName;

    public static GifticonBarcodeSummary of(Gifticon gifticon, String imageUrl) {
        return GifticonBarcodeSummary.builder()
                .id(gifticon.getId())
                .imageUrl(imageUrl)
                .productName(gifticon.getProduct().getProductName())
                .build();
    }

    public static GifticonBarcodeSummary from(Gifticon gifticon) {
        return GifticonBarcodeSummary.builder()
                .id(gifticon.getId())
                .imageUrl(gifticon.getImageKey())
                .productName(gifticon.getProduct().getProductName())
                .build();
    }
}

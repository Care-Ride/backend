package backend.knowhow.domain.gifticon.dto.user.response;

import backend.knowhow.domain.gifticon.dto.summary.GifticonProductSummary;
import backend.knowhow.global.common.response.PageInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public record GifticonProductListResponse(
        List<GifticonProductSummary> products,
        PageInfo pageInfo,
        int pointBalance
) {
    public static GifticonProductListResponse of(Page<GifticonProductSummary> products, int pointBalance) {
        PageInfo pageInfo = PageInfo.from(products);
        return new GifticonProductListResponse(products.getContent(), pageInfo, pointBalance);
    }
}

package backend.knowhow.domain.gifticon.dto.user.response;

import backend.knowhow.domain.gifticon.dto.summary.GifticonProductSummary;
import backend.knowhow.global.common.response.PageInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public record GifticonProductListResponse(
        List<GifticonProductSummary> products,
        PageInfo pageInfo
) {
    public static GifticonProductListResponse from(Page<GifticonProductSummary> products) {
        PageInfo pageInfo = PageInfo.from(products);
        return new GifticonProductListResponse(products.getContent(), pageInfo);
    }
}

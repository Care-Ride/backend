package backend.knowhow.domain.gifticon.dto.user.response;

import backend.knowhow.domain.gifticon.dto.summary.GifticonHistorySummary;
import backend.knowhow.global.common.response.PageInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public record GifticonHistoryListResponse(
        List<GifticonHistorySummary> histories,
        PageInfo pageInfo
){
    public static GifticonHistoryListResponse from(Page<GifticonHistorySummary> products) {
        PageInfo pageInfo = PageInfo.from(products);
        return new GifticonHistoryListResponse(products.getContent(), pageInfo);
    }
}

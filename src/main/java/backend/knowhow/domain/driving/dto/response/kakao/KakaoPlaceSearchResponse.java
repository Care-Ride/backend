package backend.knowhow.domain.driving.dto.response.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class KakaoPlaceSearchResponse {

    private List<Document> documents;
    private Meta meta;

    @Getter
    public static class Document {
        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("road_address_name")
        private String roadAddressName;

        private String x; // 경도
        private String y; // 위도

        @JsonProperty("place_url")
        private String placeUrl;

        @JsonProperty("category_name")
        private String categoryName;
    }

    @Getter
    public static class Meta {
        @JsonProperty("is_end")
        private boolean isEnd;

        @JsonProperty("pageable_count")
        private int pageableCount;

        @JsonProperty("total_count")
        private int totalCount;
    }
}

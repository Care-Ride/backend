package backend.knowhow.domain.driving.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PlaceSearchListResponse {
    private List<PlaceSearch> places;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PlaceSearch {
        private String name;
        private String address;
        private String roadAddress;
        private String lat;   // 위도 - y
        private String lon;  // 경도 - x
    }
}

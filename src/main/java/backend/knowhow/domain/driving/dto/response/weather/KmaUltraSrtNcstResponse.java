package backend.knowhow.domain.driving.dto.response.weather;

import lombok.Data;

import java.util.List;

@Data
public class KmaUltraSrtNcstResponse {

    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private String dataType;
        private Items items;
        private int pageNo;
        private int numOfRows;
        private int totalCount;
    }

    @Data
    public static class Items {
        private List<Item> item;
    }

    @Data
    public static class Item {
        private String baseDate;
        private String baseTime;
        private String category;   // T1H, REH, WSD, RN1, PTY 등
        private String obsrValue;  // 관측값
        private int nx;
        private int ny;
    }
}
package backend.knowhow.domain.alert.dto.external;

import lombok.Data;

import java.util.List;

@Data
public class CautionSectionResponse {

    private Body body;

    @Data
    public static class Body {
        private List<CautionSectionItem> items;
    }
}

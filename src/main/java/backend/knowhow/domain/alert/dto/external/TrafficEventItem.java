package backend.knowhow.domain.alert.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrafficEventItem {
    private String type;
    private String eventType;
    private String eventDetailType;
    private String startDate;
    private double lat;
    private double lon;
    private String linkId;
    private String roadName;
    private String roadNo;
    private String roadDrcType;
    private String lanesBlocked;
    private String message;

}

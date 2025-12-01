package backend.knowhow.domain.alert.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

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

    public static TrafficEventItem fromMap(Map<String, Object> map) {
        return new TrafficEventItem(
                (String) map.get("type"),
                (String) map.get("eventType"),
                (String) map.get("eventDetailType"),
                (String) map.get("startDate"),
                Double.parseDouble((String) map.get("coordY")),
                Double.parseDouble((String) map.get("coordX")),
                (String) map.get("linkId"),
                (String) map.get("roadName"),
                (String) map.get("roadNo"),
                (String) map.get("roadDrcType"),
                (String) map.get("lanesBlocked"),
                (String) map.get("message")
        );
    }
}

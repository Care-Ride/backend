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
public class CautionSectionItem {

    private String message;
    private String outbrkType;     // 사고잦은구간, 갓길차로 등
    private String priority;
    private String routeName;
    private String routeNo;
    private String roadDrcType;

    private String startStdLinkId;
    private double startX;
    private double startY;

    private String revRouteName;
    private String revRouteNo;
    private String revRoadDrcType;
    private String revStdLinkId;
    private double revX;
    private double revY;

    private String occrrncId;

    public static CautionSectionItem fromMap(Map<String, Object> map) {
        return CautionSectionItem.builder()
                .message((String) map.get("message"))
                .outbrkType((String) map.get("outbrkType"))
                .priority((String) map.get("priority"))
                .routeName((String) map.get("routeName"))
                .routeNo((String) map.get("routeNo"))
                .roadDrcType((String) map.get("roadDrcType"))
                .startStdLinkId((String) map.get("startStdLinkId"))
                .startX(Double.parseDouble((String) map.get("startX")))
                .startY(Double.parseDouble((String) map.get("startY")))
                .revRouteName((String) map.get("revRouteName"))
                .revRouteNo((String) map.get("revRouteNo"))
                .revRoadDrcType((String) map.get("revRoadDrcType"))
                .revStdLinkId((String) map.get("revStdLinkId"))
                .revX(Double.parseDouble((String) map.get("revX")))
                .revY(Double.parseDouble((String) map.get("revY")))
                .occrrncId((String) map.get("occrrncId"))
                .build();
    }
}

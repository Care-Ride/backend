package backend.knowhow.domain.alert.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
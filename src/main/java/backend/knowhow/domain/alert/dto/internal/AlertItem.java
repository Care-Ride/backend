package backend.knowhow.domain.alert.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertItem {

    public enum SourceType {
        TRAFFIC_EVENT,       // 돌발상황 (trafficEvent)
        CAUTION_SECTION,     // 주의운전구간 (cautionSection)
        TRAFFIC_FLOW,        // 소통정보 (trafficInfo)
        ACCIDENT_SPOT        // 사고 다발 구역 (koroadRiskService)
    }

    private SourceType source;
    private String message;      // 음성으로 읽어줄 문구 ("전방 300m 앞 공사구간")
    private double distance;     // 현재 위치와의 거리 (m 단위)
}

package backend.knowhow.domain.traffic.dto;

import lombok.Getter;

@Getter
public class LocationMessage {
    private double lat;
    private double lon;
    private Long timestamp;
}

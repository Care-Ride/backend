package backend.knowhow.domain.alert.client;

import backend.knowhow.domain.alert.dto.external.TrafficEventItem;
import backend.knowhow.domain.alert.dto.external.TrafficEventResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class TrafficEventApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${traffic.api.key}")
    private String apiKey;

    @Value("${traffic.api.base-url}")
    private String baseUrl;

    public List<TrafficEventItem> fetchEvents(double minX, double maxX, double minY, double maxY) {
        String url = String.format(
                "%s/eventInfo?apiKey=%s&type=all&eventType=all&minX=%.6f&maxX=%.6f&minY=%.6f&maxY=%.6f&getType=json",
                baseUrl, apiKey, minX, maxX, minY, maxY
        );

        TrafficEventResponse response = restTemplate.getForObject(url, TrafficEventResponse.class);

        if (response == null || response.getBody() == null) {
            return List.of();
        }

        List<TrafficEventItem> items = response.getBody().getItems();
        return items != null ? items : List.of();
    }
}

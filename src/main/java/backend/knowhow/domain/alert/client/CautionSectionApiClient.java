package backend.knowhow.domain.alert.client;

import backend.knowhow.domain.alert.dto.external.CautionSectionItem;
import backend.knowhow.domain.alert.dto.external.CautionSectionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CautionSectionApiClient {

    private final RestTemplate restTemplate;

    @Value("${traffic.api.key}")
    private String apiKey;

    @Value("${traffic.api.base-url}")
    private String baseUrl;

    public List<CautionSectionItem> fetchCautionSections(double minX, double maxX, double minY, double maxY) {

        String url = String.format(
                "%s/posIncidentInfo?apiKey=%s&minX=%.6f&maxX=%.6f&minY=%.6f&maxY=%.6f&getType=json",
                baseUrl, apiKey, minX, maxX, minY, maxY
        );

        try {
            CautionSectionResponse response =
                    restTemplate.getForObject(url, CautionSectionResponse.class);

            if (response == null || response.getBody() == null) {
                log.warn("[ItsApiClient] Empty response from caution section API");
                return List.of();
            }

            List<CautionSectionItem> items = response.getBody().getItems();
            return items != null ? items : List.of();

        } catch (RestClientException e) {
            log.error("[ItsApiClient] Failed to fetch caution sections: minX={}, maxX={}, minY={}, maxY={}",
                    minX, maxX, minY, maxY, e);
            return List.of();
        }
    }
}

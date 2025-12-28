package backend.knowhow.domain.mobility.service;

import backend.knowhow.domain.mobility.domain.MobilityType;
import backend.knowhow.domain.mobility.domain.RegionAreaCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MobilityService {

    private final ReverseGeoApiClient reverseGeoApiClient;

    private static final String TAXI_UNITED_NUMBER = "1333";
    private static final String SUBSTITUTE_NUMBER = "1588-0000";

    public String getPhoneNumber(double lat, double lon, MobilityType type) {
        if (type == MobilityType.SUBSTITUTE) {
            return SUBSTITUTE_NUMBER;
        }
        // 지역 조회나 매핑 실패 시 전국 공통 번호(1333) 반환
        String areaCode = reverseGeoApiClient.findRegionName(lat, lon)
                .flatMap(RegionAreaCode::findCodeByName)
                .orElse("");

        return areaCode + TAXI_UNITED_NUMBER;

    }
}

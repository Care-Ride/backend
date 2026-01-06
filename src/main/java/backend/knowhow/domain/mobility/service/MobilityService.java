package backend.knowhow.domain.mobility.service;

import backend.knowhow.domain.mobility.domain.MobilityType;
import backend.knowhow.domain.mobility.domain.RegionAreaCode;
import backend.knowhow.domain.mobility.dto.MobilityResponse;
import backend.knowhow.domain.mobility.dto.RegionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MobilityService {

    private final ReverseGeoApiClient reverseGeoApiClient;

    private static final String TAXI_UNITED_NUMBER = "114";
    private static final String SUBSTITUTE_NUMBER = "1588-0000";

    public MobilityResponse getCallTaxiInfo(double lat, double lon, MobilityType type) {
        if (type == MobilityType.SUBSTITUTE) {
            return new MobilityResponse(
                    SUBSTITUTE_NUMBER,
                    "전국 어디서나 이용 가능한 대리운전 번호로 연결합니다."
            );
        }
        // 지역 조회나 매핑 실패 시 전국 공통 번호(1333) 반환
        RegionInfo region = reverseGeoApiClient.findRegionInfo(lat, lon).orElse(null);
        String phoneNumber;
        String description;

        if (region == null) {
            phoneNumber = TAXI_UNITED_NUMBER;
            description = "전국 어디서나 이용 가능한 콜택시 번호로 연결합니다.";
        } else {
            String areaCode = RegionAreaCode
                    .findCodeByName(region.city())
                    .orElse("");

            phoneNumber = areaCode + TAXI_UNITED_NUMBER;

            String locationText = region.city();
            if (region.district() != null && !region.district().isBlank()) {
                locationText += " " + region.district();
            }
            if (region.dong() != null && !region.dong().isBlank()) {
                locationText += " " + region.dong();
            }

            description =
                    locationText + "에서 부를 수 있는 콜택시 번호로 연결합니다.";
        }

        return new MobilityResponse(phoneNumber, description);
    }
}

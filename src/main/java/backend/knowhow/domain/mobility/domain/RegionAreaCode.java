package backend.knowhow.domain.mobility.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum RegionAreaCode {
    SEOUL("서울특별시", "02"),
    GYEONGGI("경기도", "031"),
    INCHEON("인천광역시", "032"),
    GANGWON("강원특별자치도", "033"),
    CHUNGNAM("충청남도", "041"),
    DAEJEON("대전광역시", "042"),
    CHUNGBUK("충청북도", "043"),
    SEJONG("세종특별자치시", "044"),
    BUSAN("부산광역시", "051"),
    ULSAN("울산광역시", "052"),
    DAEGU("대구광역시", "053"),
    GYEONGBUK("경상북도", "054"),
    GYEONGNAM("경상남도", "055"),
    JEONNAM("전라남도", "061"),
    GWANGJU("광주광역시", "062"),
    JEONBUK("전북특별자치도", "063"),
    JEJU("제주특별자치도", "064");

    private final String regionName;
    private final String areaCode;

    public static Optional<String> findCodeByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(r -> name.contains(r.regionName))
                .map(RegionAreaCode::getAreaCode)
                .findFirst();
    }
}

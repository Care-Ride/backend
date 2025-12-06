package backend.knowhow.domain.alert.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "koroad_hotspot")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KoroadHotspot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * koroad 타입
     * - OLD_MAN : 보행노인
     * - CHILD   : 보행어린이
     * - SCHOOL  : 어린이보호구역
     */
    @Column(nullable = false, length = 20)
    private String type;

    @Column(name = "afos_fid", length = 50)
    private String afosFid;

    @Column(name = "spot_name", length = 200)
    private String spotName;

    @Column(name = "sido_sgg_name", length = 100)
    private String sidoSggName;

    // Koroad API 호출 시 사용했던 시/도, 구/군 코드 (검색 범위 관리용)
    private Integer siDo;
    private Integer guGun;

    @Column(name = "accident_count")
    private Integer accidentCount;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    // Koroad에서 내려주는 geom_json 그대로 저장 (GeoJSON 문자열)
    @Lob
    @Column(name = "geom_json", columnDefinition = "TEXT")
    private String geomJson;
}


package backend.knowhow.domain.alert.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class KoroadBaseResponse {

    private String resultCode;
    private String resultMsg;

    private Items items;

    private int totalCount;
    private int numOfRows;
    private int pageNo;

    @Getter
    public static class Items {
        private List<Item> item;
    }

    @Getter
    public static class Item {

        @JsonProperty("afos_fid")
        private Long afosFid;

        @JsonProperty("afos_id")
        private String afosId;

        @JsonProperty("bjd_cd")
        private String bjdCd;

        @JsonProperty("spot_cd")
        private String spotCd;

        @JsonProperty("sido_sgg_nm")
        private String sidoSggName;

        @JsonProperty("spot_nm")
        private String spotName;

        @JsonProperty("occrrnc_cnt")
        private Integer accidentCount;

        @JsonProperty("caslt_cnt")
        private Integer casltCnt;

        @JsonProperty("dth_dnv_cnt")
        private Integer dthDnvCnt;

        @JsonProperty("se_dnv_cnt")
        private Integer seDnvCnt;

        @JsonProperty("sl_dnv_cnt")
        private Integer slDnvCnt;

        @JsonProperty("wnd_dnv_cnt")
        private Integer wndDnvCnt;

        @JsonProperty("geom_json")
        private String geomJson;

        @JsonProperty("lo_crd")
        private String loCrd;   // "127.0..." 문자열로 옴

        @JsonProperty("la_crd")
        private String laCrd;   // "37.6..." 문자열
    }

    // 편의 메서드
    public List<Item> getItemList() {
        if (items == null || items.item == null) return List.of();
        return items.item;
    }
}
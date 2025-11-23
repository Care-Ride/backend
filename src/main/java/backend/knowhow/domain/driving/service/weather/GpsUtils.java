package backend.knowhow.domain.driving.service.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GpsUtils {
    public static final int TO_GRID = 0;
    public static final int TO_GPS  = 1;

    // 위도경도 ↔ 기상청 격자값
    public static LatXLngY convertGRID_GPS(int mode, double lat, double lng) {
        // 기상청 동네예보용 좌표계 파라미터
        double RE   = 6371.00877; // 지구 반경(km)
        double GRID = 5.0;        // 격자 간격(km)
        double SLAT1 = 30.0;      // 투영 위도1 (deg)
        double SLAT2 = 60.0;      // 투영 위도2 (deg)
        double OLON  = 126.0;     // 기준점 경도 (deg)
        double OLAT  = 38.0;      // 기준점 위도 (deg)
        double XO    = 43.0;      // 기준점 X좌표
        double YO    = 136.0;     // 기준점 Y좌표

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re   = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon  = OLON * DEGRAD;
        double olat  = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5)
                / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);

        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;

        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        LatXLngY rs = new LatXLngY();

        if (mode == TO_GRID) {
            // 위경도 -> 격자
            rs.lat = lat;
            rs.lng = lng;

            double ra = Math.tan(Math.PI * 0.25 + (lat) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);

            double theta = lng * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;

            rs.nx = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.ny = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

            log.info("nx: {}", rs.nx);
            log.info("ny: {}", rs.ny);
        } else if(mode == TO_GPS) {
            // 격자 -> 위경도
            rs.nx = lat;
            rs.ny = lng;

            double xn = lat - XO;
            double yn = ro - lng + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);

            if (sn < 0.0) ra = -ra;

            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) theta = -theta;
                } else {
                    theta = Math.atan2(xn, yn);
                }
            }

            double alon = theta / sn + olon;

            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }

        return rs;
    }

    public static class LatXLngY {
        public double lat;  // 위도
        public double lng;  // 경도
        public double nx;    // 격자 nx
        public double ny;    // 격자 ny
    }
}

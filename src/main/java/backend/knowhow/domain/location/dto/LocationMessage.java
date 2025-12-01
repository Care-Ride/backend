package backend.knowhow.domain.location.dto;


public record LocationMessage (
    double lat,
    double lon,
    double speed,
    double heading,
    Long timestamp
){
}

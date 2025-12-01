package backend.knowhow.domain.location.domain;

import backend.knowhow.domain.location.dto.LocationMessage;

public record Location (

        double lat,
        double lon,
        double speed,   // km/h
        double heading, // degrees
        long timestamp
){
    public static Location from(LocationMessage locationMessage) {
        return new Location(
                locationMessage.lat(),
                locationMessage.lon(),
                locationMessage.speed(),
                locationMessage.heading(),
                locationMessage.timestamp()
        );
    }
}

package backend.knowhow.domain.traffic.controller;

import backend.knowhow.domain.traffic.dto.LocationMessage;
import backend.knowhow.domain.traffic.repository.LocationStore;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationStore locationStore;

    @MessageMapping("/location/update")
    public void updateLocation(LocationMessage message, MemberPrincipal principal) {
        locationStore.updateUserLocation(principal.getId(), message.getLat(), message.getLon());
    }
}

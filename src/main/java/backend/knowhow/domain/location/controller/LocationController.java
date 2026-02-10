package backend.knowhow.domain.location.controller;

import backend.knowhow.domain.location.dto.LocationMessage;
import backend.knowhow.domain.location.repository.LocationStore;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationStore locationStore;

    @MessageMapping("/location/update")
    public void updateLocation(LocationMessage message, Principal principal) {
        Authentication authentication = (Authentication) principal;
        MemberPrincipal mp = (MemberPrincipal) authentication.getPrincipal();
        locationStore.updateUserLocation(mp.getId(), message);
    }
}

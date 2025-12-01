package backend.knowhow.domain.alert.service;

import backend.knowhow.domain.alert.AlertHistoryStore;
import backend.knowhow.domain.alert.dto.internal.AlertItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendAlerts(Long userId, List<AlertItem> alerts) {

        for (AlertItem alert : alerts) {
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/sub/traffic",
                    alert
            );
        }
    }
}

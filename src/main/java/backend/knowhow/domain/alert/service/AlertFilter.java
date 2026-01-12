package backend.knowhow.domain.alert.service;

import backend.knowhow.domain.alert.dto.internal.AlertItem;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlertFilter {

    private static final double MAX_DISTANCE_M = 2000; // 2km 이내만

    public List<AlertItem> filter(List<AlertItem> items) {
        return items.stream()
                .filter(i -> i.getDistance() <= MAX_DISTANCE_M)
                .sorted(Comparator.comparingDouble(AlertItem::getDistance))
                .collect(Collectors.toList());
    }
}
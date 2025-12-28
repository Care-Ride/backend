package backend.knowhow.domain.mission.dto;

import backend.knowhow.domain.mission.domain.PointHistory;

import java.time.LocalDateTime;

public record PointHistoryResponse(
        int amount,
        int balanceAfter,
        String description,
        LocalDateTime createdAt
) {
    public static PointHistoryResponse from(PointHistory history) {
        return new PointHistoryResponse(
                history.getAmount(),
                history.getBalanceAfter(),
                history.getDescription(),
                history.getCreatedAt()
        );
    }
}

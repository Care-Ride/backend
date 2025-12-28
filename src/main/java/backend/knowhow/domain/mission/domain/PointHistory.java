package backend.knowhow.domain.mission.domain;

import backend.knowhow.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_point_history_member_created",
                columnList = "member_id, createdAt")
})
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private int amount;          // 변동 금액

    @Column(nullable = false)
    private int balanceAfter;    // 변동 후 잔액

    @Column(nullable = false)
    private String description;  // 내역명

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private PointHistory(Member member, int amount, int balanceAfter, String description) {
        this.member = member;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public static PointHistory earn(Member member, int amount, int balanceAfter, String description) {
        return new PointHistory(member, amount, balanceAfter, description);
    }

    public static PointHistory spend(Member member, int amount, int balanceAfter, String description) {
        return new PointHistory(member, -amount, balanceAfter, description);
    }
}

package backend.knowhow.domain.gifticon.domain;

import backend.knowhow.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "gifticon_usage")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class GifticonUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gifticon_id", nullable = false)
    private Gifticon gifticon;

    // 차감된 포인트
    @Column(nullable = false)
    private int spentPoints;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GifticonUsageStatus status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private GifticonUsage(Member buyer, Gifticon gifticon, int spentPoints, GifticonUsageStatus status) {
        this.buyer = buyer;
        this.gifticon = gifticon;
        this.spentPoints = spentPoints;
        this.status = status;
    }
    public static GifticonUsage success(Member buyer, Gifticon gifticon, int spentPoints) {
        return GifticonUsage.builder()
                .buyer(buyer)
                .gifticon(gifticon)
                .spentPoints(spentPoints)
                .status(GifticonUsageStatus.SUCCESS)
                .build();
    }

    public void markSuccess() {
        this.status = GifticonUsageStatus.SUCCESS;
    }

    public void markFailed() {
        this.status = GifticonUsageStatus.FAILED;
    }
}

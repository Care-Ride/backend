package backend.knowhow.domain.gifticon.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "gifticon_product")
@NoArgsConstructor(access = PROTECTED)
public class GifticonProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageKey;

    // 사용처명
    @Column(nullable = false)
    private String brandName;

    // 상품명
    @Column(nullable = false)
    private String productName;

    // 필요 point 양
    @Column(nullable = false)
    private int requiredPoint;

    // 재고개수
    @Column(nullable = false)
    private int stock;

    @Builder
    private GifticonProduct(String imageKey, String brandName, String productName, int requiredPoint, int stock){
        this.imageKey = imageKey;
        this.brandName = brandName;
        this.productName = productName;
        this.requiredPoint = requiredPoint;
        this.stock = stock;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }

    public void decreaseStock(int quantity) {
        this.stock -= quantity;
    }
}

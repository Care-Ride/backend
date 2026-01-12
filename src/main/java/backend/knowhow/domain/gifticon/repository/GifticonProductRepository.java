package backend.knowhow.domain.gifticon.repository;

import backend.knowhow.domain.gifticon.domain.GifticonProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GifticonProductRepository extends JpaRepository<GifticonProduct, Long> {

    // 재고가 0초과인 상품 리스트만 조회
    Page<GifticonProduct> findAllByStockGreaterThan(int stock, Pageable page);

    // 브랜드명과 상품명으로 조회
    Optional<GifticonProduct> findByBrandNameAndProductName(String brandName, String productName);
}

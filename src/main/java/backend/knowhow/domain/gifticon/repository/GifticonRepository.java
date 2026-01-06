package backend.knowhow.domain.gifticon.repository;

import backend.knowhow.domain.gifticon.domain.Gifticon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {
    // 아직 아무도 구매(usage)하지 않은 gifticon 중에서 product에 속한 것 1개 가져오기
    @Query("""
           select g
           from Gifticon g
           where g.product.id = :productId
             and not exists (
                 select 1
                 from GifticonUsage u
                 where u.gifticon = g
             )
           order by g.id asc
           """)
    List<Gifticon> findUnassignedByProductId(@Param("productId") Long productId, Pageable pageable);
}

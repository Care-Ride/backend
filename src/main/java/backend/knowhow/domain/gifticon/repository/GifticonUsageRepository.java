package backend.knowhow.domain.gifticon.repository;

import backend.knowhow.domain.gifticon.domain.Gifticon;
import backend.knowhow.domain.gifticon.domain.GifticonProduct;
import backend.knowhow.domain.gifticon.domain.GifticonUsage;
import backend.knowhow.domain.gifticon.domain.GifticonUsageStatus;
import backend.knowhow.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GifticonUsageRepository extends JpaRepository<GifticonUsage, Long> {
    Page<GifticonUsage> findAllByBuyerAndStatus(Member buyer, GifticonUsageStatus status, Pageable page);

    Optional<GifticonUsage> findByGifticonAndBuyer(Gifticon gifticon, Member buyer);
}

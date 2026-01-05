package backend.knowhow.domain.gifticon.repository;

import backend.knowhow.domain.gifticon.domain.Gifticon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {
}

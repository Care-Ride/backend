package backend.knowhow.domain.alert.repository;

import backend.knowhow.domain.alert.domain.KoroadHotspot;
import backend.knowhow.domain.alert.domain.KoroadType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoroadHotspotRepository extends JpaRepository<KoroadHotspot, Long> {

    List<KoroadHotspot> findByType(KoroadType type);

    List<KoroadHotspot> findByTypeAndSiDoAndGuGun(KoroadType type, Integer siDo, Integer guGun);

    void deleteByTypeAndSiDoAndGuGun(KoroadType type, Integer siDo, Integer guGun);
}

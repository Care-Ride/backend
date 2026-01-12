package backend.knowhow.domain.alert.repository;

import backend.knowhow.domain.alert.domain.KoroadHotspot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoroadHotspotRepository extends JpaRepository<KoroadHotspot, Long> {

    List<KoroadHotspot> findByType(String type);

    List<KoroadHotspot> findByTypeAndSiDoAndGuGun(String type, Integer siDo, Integer guGun);

    void deleteByTypeAndSiDoAndGuGun(String type, Integer siDo, Integer guGun);
}

package backend.knowhow.domain.vehicle.repository;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.vehicle.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByOwner(Member owner);

    Optional<Vehicle> findByOwnerIdAndActiveTrue(Long ownerId);

    boolean existsByOwner(Member owner);

    @Modifying
    @Query("update Vehicle v set v.active = false where v.owner = :owner and v.active = true")
    void deactivateAllActiveByOwner(@Param("owner") Member owner);


}

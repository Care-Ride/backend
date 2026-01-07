package backend.knowhow.domain.vehicle.domain;

import backend.knowhow.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vehicle", indexes = {
        @Index(name = "idx_vehicle_owner", columnList = "member_id")
})
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 차량 소유자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member owner;

    // 사용자 표시용 차량이름
    @Column(nullable = false, length = 50)
    private String name;

    // 차량 번호
    @Column(length = 20)
    private String carNumber;

    // BLE 디바이스 식별자
    @Column(length = 100)
    private String bleDeviceId;

    // 현재 선택된 차량 여부
    @Column(nullable = false)
    private boolean active = false;

    private Vehicle(Member owner, String name, String carNumber, String bleDeviceId) {
        this.owner = owner;
        this.name = name;
        this.carNumber = carNumber;
        this.bleDeviceId = bleDeviceId;
        this.active = false;
    }

    public static Vehicle create(Member owner, String name, String carNumber, String bleDeviceId) {
        return new Vehicle(owner, name, carNumber, bleDeviceId);
    }

    public void activate() { this.active = true; }

    public void updateInfo(String name, String carNumber) {
        this.name = name;
        this.carNumber = carNumber;
    }

    public void bindBle(String bleDeviceId) {
        this.bleDeviceId = bleDeviceId;
    }

    public void unbindBle() {
        this.bleDeviceId = null;
    }
}
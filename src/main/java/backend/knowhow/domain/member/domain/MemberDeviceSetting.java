package backend.knowhow.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "member_device_setting",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_member_device",
                        columnNames = {"member_id", "device_id"}
                )
        }
)
public class MemberDeviceSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 기기 식별자 (디바이스 ID, FCM token, UUID 등)
    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;

    // 글자 크기 (1~3), 기본 2
    @Column(name = "font_level", nullable = false)
    private int fontLevel;

    // 소리 크기 (1~3), 기본 2
    @Column(name = "volume_level", nullable = false)
    private int volumeLevel;

    @Builder
    public MemberDeviceSetting(Member member, String deviceId, int fontLevel, int volumeLevel){
        this.member = member;
        this.deviceId = deviceId;
        this.fontLevel = fontLevel;
        this.volumeLevel = volumeLevel;
    }

    // 값 수정 메서드 (범위 체크 포함)
    public void changeLevels(int fontLevel, int volumeLevel) {
        validateLevel(fontLevel, "fontLevel");
        validateLevel(volumeLevel, "volumeLevel");

        this.fontLevel = fontLevel;
        this.volumeLevel = volumeLevel;
    }

    private void validateLevel(int level, String fieldName) {
        if (level < 1 || level > 3) {
            throw new IllegalArgumentException(fieldName + " must be between 1 and 3. input=" + level);
        }
    }
}

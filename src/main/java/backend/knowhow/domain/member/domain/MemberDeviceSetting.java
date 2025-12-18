package backend.knowhow.domain.member.domain;

import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "member_device_setting")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class MemberDeviceSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 글자 크기 (1~3), 기본 2
    @Column(name = "font_level", nullable = false)
    private int fontLevel;

    // 소리 크기 (1~3), 기본 2
    @Column(name = "volume_level", nullable = false)
    private int volumeLevel;

    @Builder
    public MemberDeviceSetting(Member member, String deviceId, int fontLevel, int volumeLevel){
        this.member = member;
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
        if (level < 1 || level > 5) {
            throw new BaseException(ErrorType.INVALID_SETTING_LEVEL);
        }
    }
}

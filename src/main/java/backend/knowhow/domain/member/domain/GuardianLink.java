package backend.knowhow.domain.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"guardian_id", "senior_id"}))
public class GuardianLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guardian_id", nullable = false)
    private Member guardian;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id", nullable = false)
    private Member senior;

    // 보호자가 설정한 관계
    @Enumerated(EnumType.STRING)
    private RelationType relationType;

    // 보호자가 입력한 고령자 이름
    private String customSeniorName;

    public GuardianLink(Member guardian, Member senior) {
        this.guardian = guardian;
        this.senior = senior;
    }

    public void updateInfo(RelationType relationType, String customSeniorName) {
        this.relationType = relationType;
        this.customSeniorName = customSeniorName;
    }

}

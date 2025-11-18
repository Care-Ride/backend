package backend.knowhow.domain.auth.domain;

import jakarta.persistence.*;


@Entity
public class GuardianLink {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member guardian;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member senior;

}

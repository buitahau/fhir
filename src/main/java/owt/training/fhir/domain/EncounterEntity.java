package owt.training.fhir.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import owt.training.fhir.constant.enums.EncounterClassCodeEnum;
import owt.training.fhir.constant.enums.EncounterStatusEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "encounter")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EncounterEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -9038596596321720848L;

    @Enumerated(EnumType.STRING)
    @Column
    private EncounterStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "class_code")
    private EncounterClassCodeEnum classCode;

    @ManyToOne
    @JoinColumn(name = "episode_of_care_id", nullable = false)
    private EpisodeOfCareEntity episodeOfCare;

    @Column
    private Instant periodStart;

    @Column
    private Instant periodEnd;

    @ManyToOne
    @JoinColumn(name = "practitioner_id", nullable = false)
    private PractitionerEntity practitioner;

    @Column
    private String reasonSystem;

    @Column
    private String reasonCode;

    @Column
    private String reasonDisplay;
}

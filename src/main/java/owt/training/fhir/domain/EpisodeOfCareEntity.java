package owt.training.fhir.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import owt.training.fhir.constant.enums.EpisodeOfCareStatusEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "episode_of_care")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EpisodeOfCareEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8394377766007114745L;

    @Enumerated(EnumType.STRING)
    @Column
    private EpisodeOfCareStatusEnum status;

    @Column
    private Instant periodStart;

    @Column
    private Instant periodEnd;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;
}

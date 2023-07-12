package owt.training.fhir.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "practitioner")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PractitionerEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7662431459297565102L;

    @Column
    private String name;
}

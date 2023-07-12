package owt.training.fhir.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import owt.training.fhir.constant.enums.GenderEnum;
import owt.training.fhir.constant.enums.MaritalStatusEnum;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "patient")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientEntity implements Serializable {

    private static final long serialVersionUID = -2116740706126973037L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    @Column
    private String name;

    @Column
    private String telecom;

    @Enumerated(EnumType.STRING)
    @Column
    private GenderEnum gender;

    @Column
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital")
    private MaritalStatusEnum maritalStatus;
}

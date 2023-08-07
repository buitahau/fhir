package owt.training.fhir.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import owt.training.fhir.constant.enums.GenderEnum;
import owt.training.fhir.constant.enums.MaritalStatusEnum;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "patient")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2116740706126973037L;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelecom() {
        return telecom;
    }

    public void setTelecom(String telecom) {
        this.telecom = telecom;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MaritalStatusEnum getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatusEnum maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
}

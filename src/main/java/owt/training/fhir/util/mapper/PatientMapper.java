package owt.training.fhir.util.mapper;

import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import owt.training.fhir.domain.PatientEntity;

import java.util.ArrayList;
import java.util.List;

public class PatientMapper {

    public static PatientEntity convert(Patient patient) {
        PatientEntity entity = new PatientEntity();
        entity.setName(patient.getName().get(0).getNameAsSingleString());
        entity.setId(patient.getId());
        return entity;
    }

    public static Patient convert(PatientEntity entity) {
        Patient patient = new Patient();

        List<HumanName> names = new ArrayList<>();
        names.add(new HumanName().setFamily(entity.getName()));
        patient.setName(names);

        patient.setId(entity.getId());

        return patient;
    }
}

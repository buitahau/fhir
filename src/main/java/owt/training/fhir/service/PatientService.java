package owt.training.fhir.service;

import org.hl7.fhir.r4.model.Patient;
import owt.training.fhir.domain.PatientEntity;

import java.util.Optional;

public interface PatientService {
    Optional<PatientEntity> findById(String id);

    PatientEntity create(Patient thePatient);
}

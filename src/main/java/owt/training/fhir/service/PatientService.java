package owt.training.fhir.service;

import owt.training.fhir.domain.PatientEntity;

public interface PatientService {
    void save(PatientEntity patientEntity);
}

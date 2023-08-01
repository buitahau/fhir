package owt.training.fhir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import owt.training.fhir.domain.PatientEntity;
import owt.training.fhir.repository.PatientRepository;
import owt.training.fhir.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public void save(PatientEntity patientEntity) {
        patientRepository.save(patientEntity);
    }
}

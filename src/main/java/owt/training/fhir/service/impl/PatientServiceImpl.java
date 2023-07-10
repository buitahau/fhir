package owt.training.fhir.service.impl;

import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;
import owt.training.fhir.domain.PatientEntity;
import owt.training.fhir.repository.PatientRepository;
import owt.training.fhir.service.PatientService;
import owt.training.fhir.util.mapper.PatientMapper;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Optional<PatientEntity> findById(String id) {
        return patientRepository.findById(id);
    }

    @Override
    public PatientEntity create(Patient thePatient) {
        PatientEntity entity = PatientMapper.convert(thePatient);
        return patientRepository.save(entity);
    }
}

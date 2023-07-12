package owt.training.fhir.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import owt.training.fhir.domain.PatientEntity;
import owt.training.fhir.repository.PatientRepository;
import owt.training.fhir.service.PatientService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Optional<PatientEntity> findById(String id) {
        return patientRepository.findById(id);
    }

    @Override
    public PatientEntity create(PatientEntity entity) {
        validate(entity);
        entity.setId(UUID.randomUUID().toString());
        return patientRepository.save(entity);
    }

    private void validate(PatientEntity entity) {
        if (StringUtils.isBlank(entity.getName())) {
            throw new RuntimeException("Name is empty");
        }
    }

    @Override
    public PatientEntity update(String id, PatientEntity updateEntity) {
        PatientEntity existingEntity = findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
        validate(updateEntity);
        updateFields(existingEntity, updateEntity);
        return patientRepository.save(existingEntity);
    }

    @Override
    public void delete(String id) {
        validateDeleting(id);
        patientRepository.deleteById(id);
    }

    @Override
    public List<PatientEntity> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Page<PatientEntity> findAll(Example<PatientEntity> example, Pageable pageable) {
        return patientRepository.findAll(example, pageable);
    }

    private void validateDeleting(String id) {
        findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    private void updateFields(PatientEntity existingEntity, PatientEntity updateEntity) {
        existingEntity.setName(updateEntity.getName());
    }
}

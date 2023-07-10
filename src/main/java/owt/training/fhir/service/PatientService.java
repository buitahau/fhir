package owt.training.fhir.service;

import org.hl7.fhir.r4.model.Patient;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import owt.training.fhir.domain.PatientEntity;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Optional<PatientEntity> findById(String id);

    PatientEntity create(PatientEntity entity);

    PatientEntity update(String id, PatientEntity entity);

    void delete(String id);

    List<PatientEntity> findAll();

    Page<PatientEntity> findAll(Example<PatientEntity> example, Pageable pageable);
}

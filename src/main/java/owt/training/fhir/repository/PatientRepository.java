package owt.training.fhir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owt.training.fhir.domain.PatientEntity;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, String> {
}

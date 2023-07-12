package owt.training.fhir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owt.training.fhir.domain.PractitionerEntity;

@Repository
public interface PractitionerRepository extends JpaRepository<PractitionerEntity, String> {
}

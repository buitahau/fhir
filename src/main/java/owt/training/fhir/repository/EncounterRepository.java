package owt.training.fhir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owt.training.fhir.domain.EncounterEntity;

@Repository
public interface EncounterRepository extends JpaRepository<EncounterEntity, String> {
}

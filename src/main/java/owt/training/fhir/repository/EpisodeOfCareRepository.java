package owt.training.fhir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import owt.training.fhir.domain.EpisodeOfCareEntity;

@Repository
public interface EpisodeOfCareRepository extends JpaRepository<EpisodeOfCareEntity, String> {
}

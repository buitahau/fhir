package owt.training.fhir.service;

import owt.training.fhir.domain.EpisodeOfCareEntity;

import java.util.Optional;

public interface EpisodeOfCareService {
    EpisodeOfCareEntity create(EpisodeOfCareEntity entity);

    Optional<EpisodeOfCareEntity> findById(String id);
}

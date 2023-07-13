package owt.training.fhir.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import owt.training.fhir.domain.EpisodeOfCareEntity;

import java.util.Optional;

public interface EpisodeOfCareService {
    EpisodeOfCareEntity create(EpisodeOfCareEntity entity);

    Optional<EpisodeOfCareEntity> findById(String id);

    Page<EpisodeOfCareEntity> findAll(Example<EpisodeOfCareEntity> episodeOfCareEntityExample, Pageable pageable);

    EpisodeOfCareEntity update(String id, EpisodeOfCareEntity updateEntity);
}

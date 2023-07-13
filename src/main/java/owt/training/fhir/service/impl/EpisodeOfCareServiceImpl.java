package owt.training.fhir.service.impl;

import org.springframework.stereotype.Service;
import owt.training.fhir.domain.EpisodeOfCareEntity;
import owt.training.fhir.repository.EpisodeOfCareRepository;
import owt.training.fhir.service.EpisodeOfCareService;

import java.util.Optional;
import java.util.UUID;

@Service
public class EpisodeOfCareServiceImpl implements EpisodeOfCareService {

    private final EpisodeOfCareRepository episodeOfCareRepository;

    public EpisodeOfCareServiceImpl(EpisodeOfCareRepository episodeOfCareRepository) {
        this.episodeOfCareRepository = episodeOfCareRepository;
    }

    @Override
    public EpisodeOfCareEntity create(EpisodeOfCareEntity entity) {
        validate(entity);
        entity.setId(UUID.randomUUID().toString());
        return episodeOfCareRepository.save(entity);
    }

    @Override
    public Optional<EpisodeOfCareEntity> findById(String id) {
        return episodeOfCareRepository.findById(id);
    }

    private void validate(EpisodeOfCareEntity entity) {

    }
}

package owt.training.fhir.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<EpisodeOfCareEntity> findAll(Example<EpisodeOfCareEntity> episodeOfCareEntityExample,
                                             Pageable pageable) {

        return episodeOfCareRepository.findAll(episodeOfCareEntityExample, pageable);
    }

    @Override
    public EpisodeOfCareEntity update(String id, EpisodeOfCareEntity updateEntity) {
        EpisodeOfCareEntity existingEntity = findById(id).orElseThrow(() -> new RuntimeException("Episode not found"));
        validate(updateEntity);
        updateFields(existingEntity, updateEntity);
        return episodeOfCareRepository.save(existingEntity);
    }

    private void updateFields(EpisodeOfCareEntity existingEntity, EpisodeOfCareEntity updateEntity) {
        existingEntity.setStatus(updateEntity.getStatus());
        existingEntity.setPatient(updateEntity.getPatient());
        existingEntity.setPeriodStart(updateEntity.getPeriodStart());
        existingEntity.setPeriodEnd(updateEntity.getPeriodEnd());
    }

    private void validate(EpisodeOfCareEntity entity) {

    }
}

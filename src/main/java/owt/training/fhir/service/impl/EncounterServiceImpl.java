package owt.training.fhir.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import owt.training.fhir.domain.EncounterEntity;
import owt.training.fhir.repository.EncounterRepository;
import owt.training.fhir.service.EncounterService;

import java.util.Optional;
import java.util.UUID;

@Service
public class EncounterServiceImpl implements EncounterService {

    private final EncounterRepository encounterRepository;

    public EncounterServiceImpl(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    @Override
    public EncounterEntity create(EncounterEntity entity) {
        validate(entity);
        entity.setId(UUID.randomUUID().toString());
        return encounterRepository.save(entity);
    }

    @Override
    public Optional<EncounterEntity> findById(String id) {
        return encounterRepository.findById(id);
    }

    @Override
    public Page<EncounterEntity> findAll(Example<EncounterEntity> encounterEntityExample, Pageable pageable) {
        return encounterRepository.findAll(encounterEntityExample, pageable);
    }

    @Override
    public EncounterEntity update(String id, EncounterEntity updateEntity) {
        EncounterEntity existingEntity = findById(id).orElseThrow(() -> new RuntimeException("Encounter not found"));
        validate(updateEntity);
        updateFields(existingEntity, updateEntity);
        return encounterRepository.save(existingEntity);
    }

    private void updateFields(EncounterEntity existingEntity, EncounterEntity updateEntity) {
        existingEntity.setStatus(updateEntity.getStatus());
    }

    private void validate(EncounterEntity entity) {
    }
}

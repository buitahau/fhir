package owt.training.fhir.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import owt.training.fhir.domain.EncounterEntity;

import java.util.Optional;

public interface EncounterService {
    EncounterEntity create(EncounterEntity entity);

    Optional<EncounterEntity> findById(String id);

    Page<EncounterEntity> findAll(Example<EncounterEntity> encounterEntityExample, Pageable pageable);

    EncounterEntity update(String id, EncounterEntity updateEntity);
}

package owt.training.fhir.service;

import owt.training.fhir.domain.PractitionerEntity;

import java.util.Optional;

public interface PractitionerService {
    PractitionerEntity create(PractitionerEntity entity);

    Optional<PractitionerEntity> findById(String id);
}

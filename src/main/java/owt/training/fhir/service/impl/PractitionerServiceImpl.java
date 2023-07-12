package owt.training.fhir.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import owt.training.fhir.domain.PractitionerEntity;
import owt.training.fhir.repository.PractitionerRepository;
import owt.training.fhir.service.PractitionerService;

import java.util.Optional;
import java.util.UUID;

@Service
public class PractitionerServiceImpl implements PractitionerService {

    private PractitionerRepository practitionerRepository;

    public PractitionerServiceImpl(PractitionerRepository practitionerRepository) {
        this.practitionerRepository = practitionerRepository;
    }

    @Override
    public PractitionerEntity create(PractitionerEntity entity) {
        validate(entity);
        entity.setId(UUID.randomUUID().toString());
        return practitionerRepository.save(entity);
    }

    @Override
    public Optional<PractitionerEntity> findById(String id) {
        return practitionerRepository.findById(id);
    }

    private void validate(PractitionerEntity entity) {
        if (StringUtils.isBlank(entity.getName())) {
            throw new RuntimeException("Name is empty");
        }
    }
}

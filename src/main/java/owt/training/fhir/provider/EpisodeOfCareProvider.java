package owt.training.fhir.provider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.validation.FhirValidator;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.stereotype.Component;
import owt.training.fhir.domain.EpisodeOfCareEntity;
import owt.training.fhir.service.EpisodeOfCareService;
import owt.training.fhir.util.MethodOutcomeUtil;
import owt.training.fhir.util.mapper.EpisodeOfCareMapper;

@Component
public class EpisodeOfCareProvider extends BaseProvider{

    private final EpisodeOfCareService episodeOfCareService;

    public EpisodeOfCareProvider(FhirValidator fhirValidator, EpisodeOfCareService episodeOfCareService) {
        super(fhirValidator);
        this.episodeOfCareService = episodeOfCareService;
    }

    @Override
    public Class<EpisodeOfCare> getResourceType() {
        return EpisodeOfCare.class;
    }

    @Read
    public EpisodeOfCare findById(@IdParam IdType theId) {
        EpisodeOfCareEntity entity = episodeOfCareService.findById(theId.getIdPart())
                .orElseThrow(() -> new ResourceNotFoundException(theId));
        return EpisodeOfCareMapper.convert(entity);
    }

    @Create
    public MethodOutcome create(@ResourceParam EpisodeOfCare theEpisodeOfCare) {
        validate(theEpisodeOfCare);
        EpisodeOfCareEntity entity = episodeOfCareService.create(EpisodeOfCareMapper.convert(theEpisodeOfCare));
        return MethodOutcomeUtil.buildMethodOutcome(entity);
    }
}

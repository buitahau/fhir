package owt.training.fhir.provider;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.validation.FhirValidator;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import owt.training.fhir.domain.EpisodeOfCareEntity;
import owt.training.fhir.domain.PatientEntity;
import owt.training.fhir.service.EpisodeOfCareService;
import owt.training.fhir.util.MethodOutcomeUtil;
import owt.training.fhir.util.mapper.EpisodeOfCareMapper;

@Component
public class EpisodeOfCareProvider extends BaseProvider {

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

    @Update
    public MethodOutcome update(@IdParam IdType theId,
                                @ResourceParam EpisodeOfCare theEpisodeOfCare) {

        validate(theEpisodeOfCare);
        EpisodeOfCareEntity entity = episodeOfCareService.update(theId.getIdPart(),
                EpisodeOfCareMapper.convert(theEpisodeOfCare));
        return MethodOutcomeUtil.buildMethodOutcome(entity);
    }

    @Search
    public IBundleProvider search(@RequiredParam(name = EpisodeOfCare.SP_PATIENT) StringParam thePatient,
                                  @Offset Integer theOffset, @Count Integer theCount) {

        String patientId = thePatient.getValue();
        Page<EpisodeOfCareEntity> result = episodeOfCareService.findAll(buildExample(patientId),
                buildPageable(theOffset, theCount));
        return buildIBundleProvider(result, EpisodeOfCareMapper::convert);
    }

    private Example<EpisodeOfCareEntity> buildExample(String patientId) {
        EpisodeOfCareEntity entity = new EpisodeOfCareEntity();

        PatientEntity patient = new PatientEntity();
        patient.setId(patientId);
        entity.setPatient(patient);

        return Example.of(entity);
    }
}

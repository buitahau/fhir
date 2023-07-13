package owt.training.fhir.provider;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.validation.FhirValidator;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import owt.training.fhir.domain.EncounterEntity;
import owt.training.fhir.domain.EpisodeOfCareEntity;
import owt.training.fhir.domain.PatientEntity;
import owt.training.fhir.service.EncounterService;
import owt.training.fhir.util.MethodOutcomeUtil;
import owt.training.fhir.util.mapper.EncounterMapper;

@Component
public class EncounterProvider extends BaseProvider {

    private EncounterService encounterService;

    public EncounterProvider(FhirValidator fhirValidator, EncounterService encounterService) {
        super(fhirValidator);
        this.encounterService = encounterService;
    }

    @Override
    public Class<Encounter> getResourceType() {
        return Encounter.class;
    }

    @Read
    public Encounter findById(@IdParam IdType theId) {
        EncounterEntity entity = encounterService.findById(theId.getIdPart())
                .orElseThrow(() -> new ResourceNotFoundException(theId));
        return EncounterMapper.convert(entity);
    }

    @Create
    public MethodOutcome create(@ResourceParam Encounter theEncounter) {
        validate(theEncounter);
        EncounterEntity entity = encounterService.create(EncounterMapper.convert(theEncounter));
        return MethodOutcomeUtil.buildMethodOutcome(entity);
    }

    @Update
    public MethodOutcome update(@IdParam IdType theId,
                                @ResourceParam Encounter theEncounter) {

        validate(theEncounter);
        EncounterEntity entity = encounterService.update(theId.getIdPart(), EncounterMapper.convert(theEncounter));
        return MethodOutcomeUtil.buildMethodOutcome(entity);
    }

    @Search
    public IBundleProvider search(@RequiredParam(name = Encounter.SP_PATIENT) StringParam thePatient,
                                  @Offset Integer theOffset, @Count Integer theCount) {

        String patientId = thePatient.getValue();
        Page<EncounterEntity> result = encounterService.findAll(buildExample(patientId),
                buildPageable(theOffset, theCount));
        return buildIBundleProvider(result, EncounterMapper::convert);
    }

    private Example<EncounterEntity> buildExample(String patientId) {
        EncounterEntity encounter = new EncounterEntity();

        EpisodeOfCareEntity episodeOfCare = new EpisodeOfCareEntity();
        encounter.setEpisodeOfCare(episodeOfCare);

        PatientEntity patient = new PatientEntity();
        patient.setId(patientId);
        episodeOfCare.setPatient(patient);

        return Example.of(encounter);
    }
}

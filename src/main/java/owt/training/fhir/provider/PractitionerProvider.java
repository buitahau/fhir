package owt.training.fhir.provider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.validation.FhirValidator;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.stereotype.Component;
import owt.training.fhir.domain.PractitionerEntity;
import owt.training.fhir.service.PractitionerService;
import owt.training.fhir.util.MethodOutcomeUtil;
import owt.training.fhir.util.mapper.PractitionerMapper;

@Component
public class PractitionerProvider extends BaseProvider {

    private PractitionerService practitionerService;

    public PractitionerProvider(PractitionerService practitionerService, FhirValidator fhirValidator) {
        super(fhirValidator);
        this.practitionerService = practitionerService;
    }

    @Override
    public Class<Practitioner> getResourceType() {
        return Practitioner.class;
    }

    @Read
    public Practitioner findById(@IdParam IdType theId) {
        PractitionerEntity entity = practitionerService.findById(theId.getIdPart())
                .orElseThrow(() -> new ResourceNotFoundException(theId));
        return PractitionerMapper.convert(entity);
    }

    @Create
    public MethodOutcome create(@ResourceParam Practitioner thePractitioner) {
        validate(thePractitioner);
        PractitionerEntity entity = practitionerService.create(PractitionerMapper.convert(thePractitioner));
        return MethodOutcomeUtil.buildMethodOutcome(entity);
    }
}

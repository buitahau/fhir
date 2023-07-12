package owt.training.fhir.provider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.stereotype.Component;
import owt.training.fhir.constant.PatientConstant;
import owt.training.fhir.domain.PractitionerEntity;
import owt.training.fhir.service.PractitionerService;
import owt.training.fhir.util.mapper.PractitionerMapper;

import java.util.List;

@Component
public class PractitionerProvider implements IResourceProvider {

    private PractitionerService practitionerService;

    private FhirValidator fhirValidator;

    public PractitionerProvider(PractitionerService practitionerService, FhirValidator fhirValidator) {
        this.practitionerService = practitionerService;
        this.fhirValidator = fhirValidator;
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
        return buildMethodOutcome(entity);
    }

    private MethodOutcome buildMethodOutcome(PractitionerEntity entity) {
        MethodOutcome retVal = new MethodOutcome();
        retVal.setId(new IdType(PatientConstant.PATIENT_RESOURCE_NAME, entity.getId()));
        retVal.setResource(PractitionerMapper.convert(entity));
        return retVal;
    }

    private void validate(Practitioner thePractitioner) {
        ValidationResult validationResult = fhirValidator.validateWithResult(thePractitioner);
        if (validationResult.isSuccessful()) {
            return;
        }

        throw new InvalidRequestException(buildErrorMessage(validationResult.getMessages()));
    }

    private String buildErrorMessage(List<SingleValidationMessage> messages) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        messages.forEach(message -> errorMessageBuilder.append(message.getLocationString())
                .append(" - ")
                .append(message.getMessage())
                .append("."));
        return errorMessageBuilder.toString();
    }
}

package owt.training.fhir.provider;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import owt.training.fhir.constant.PatientConstant;
import owt.training.fhir.domain.PatientEntity;
import owt.training.fhir.service.PatientService;
import owt.training.fhir.util.mapper.PatientMapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientProvider implements IResourceProvider {

    private PatientService patientService;

    private FhirValidator fhirValidator;

    public PatientProvider(PatientService patientService, FhirValidator fhirValidator) {
        this.patientService = patientService;
        this.fhirValidator = fhirValidator;
    }

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    @Read
    public Patient findById(@IdParam IdType theId) {
        PatientEntity entity = patientService.findById(theId.getIdPart())
                .orElseThrow(() -> new ResourceNotFoundException(theId));
        return PatientMapper.convert(entity);
    }

    @Create
    public MethodOutcome create(@ResourceParam Patient thePatient) {
        validate(thePatient);
        PatientEntity entity = patientService.create(PatientMapper.convert(thePatient));
        return buildMethodOutcome(entity);
    }

    @Update
    public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient thePatient) {
        validate(thePatient);
        PatientEntity entity = patientService.update(theId.getIdPart(), PatientMapper.convert(thePatient));
        return buildMethodOutcome(entity);
    }

    @Delete
    public void delete(@IdParam IdType theId) {
        validateDelete(theId);
        patientService.delete(theId.getIdPart());
    }

    @Patch
    public OperationOutcome patch(@IdParam IdType theId, PatchTypeEnum thePatchType,
                                  @ResourceParam Patient thePatient) {

        // TODO : need more investigate
        OperationOutcome retVal = new OperationOutcome();
        retVal.getText().setDivAsString("<div>OK</div>");
        return retVal;
    }

    @Search
    public List<Patient> searchAll() {
        return patientService.findAll().stream().map(PatientMapper::convert).collect(Collectors.toList());
    }

    @Search
    public IBundleProvider searchWithPaging(@RequiredParam(name = Patient.SP_FAMILY) StringParam theFamily,
                                            @Offset Integer theOffset, @Count Integer theCount) {

        String familyToMatch = theFamily.getValue();
        Page<PatientEntity> result = patientService.findAll(buildExample(familyToMatch),
                buildPageable(theOffset, theCount));
        return buildIBundleProvider(result);
    }

    private IBundleProvider buildIBundleProvider(Page<PatientEntity> result) {
        return new IBundleProvider() {
            @Override
            public IPrimitiveType<Date> getPublished() {
                return InstantType.withCurrentTime();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                return result.get().map(PatientMapper::convert).collect(Collectors.toList());
            }

            @Nullable
            @Override
            public String getUuid() {
                return null;
            }

            @Override
            public Integer preferredPageSize() {
                return null;
            }

            @Nullable
            @Override
            public Integer size() {
                return result.getTotalPages();
            }
        };
    }

    private Pageable buildPageable(Integer offset, Integer count) {
        return PageRequest.of(offset != null ? offset : 0, count != null ? count : 20);
    }

    private Example<PatientEntity> buildExample(String name) {
        PatientEntity probe = new PatientEntity();
        probe.setName(name);
        return Example.of(probe);
    }

    private void validateDelete(IdType theId) {
        patientService.findById(theId.getIdPart())
                .orElseThrow(() -> new ResourceNotFoundException(theId));
    }

    private MethodOutcome buildMethodOutcome(PatientEntity entity) {
        MethodOutcome retVal = new MethodOutcome();
        retVal.setId(new IdType(PatientConstant.PATIENT_RESOURCE_NAME, entity.getId()));
        retVal.setResource(PatientMapper.convert(entity));
        return retVal;
    }

    private void validate(Patient thePatient) {
        ValidationResult validationResult = fhirValidator.validateWithResult(thePatient);
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

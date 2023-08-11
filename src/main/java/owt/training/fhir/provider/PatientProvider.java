package owt.training.fhir.provider;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import owt.training.fhir.domain.PatientEntity;
import owt.training.fhir.domain.elasticsearch.PatientDocument;
import owt.training.fhir.repository.elasticsearch.PatientElasticsearchRepository;
import owt.training.fhir.service.PatientService;
import owt.training.fhir.util.MethodOutcomeUtil;
import owt.training.fhir.util.mapper.PatientMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PatientProvider extends BaseProvider {

    @Autowired
    private PatientElasticsearchRepository patientElasticsearchRepository;

    private PatientService patientService;

    public PatientProvider(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    @Read
    public Patient findById(@IdParam IdType theId) {
        Patient patient = new Patient();
        patient.setId(theId.getIdPart());

//        PatientDocument patientDocument = new PatientDocument();
//        patientDocument.setId(UUID.randomUUID().toString());
//        patientDocument.setName("Hau Bui");
//        patientDocument.setTelecom("0933656289");
//        patientElasticsearchRepository.save(patientDocument);

//        Page<PatientDocument> searchResult = patientElasticsearchRepository.findByName("Hau Bui", Pageable.ofSize(10));

        return patient;
    }

    @Create
    public MethodOutcome create(@ResourceParam Patient thePatient) {
        PatientEntity entity = patientService.create(PatientMapper.convert(thePatient));
        return MethodOutcomeUtil.buildMethodOutcome(entity);
    }

    @Update
    public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient thePatient) {
        PatientEntity entity = patientService.update(theId.getIdPart(), PatientMapper.convert(thePatient));
        return MethodOutcomeUtil.buildMethodOutcome(entity);
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
        return buildIBundleProvider(result, PatientMapper::convert);
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
}

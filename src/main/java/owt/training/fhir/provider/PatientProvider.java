package owt.training.fhir.provider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import owt.training.fhir.domain.PatientEntity;
import owt.training.fhir.service.PatientService;
import owt.training.fhir.util.mapper.PatientMapper;

@Component
public class PatientProvider implements IResourceProvider {

    @Autowired
    private PatientService patientService;

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    @Read
    public Patient getById(@IdParam IdType theId) {
        PatientEntity entity = patientService.findById(theId.getId())
                .orElseThrow(() -> new ResourceNotFoundException(theId.getId()));
        return PatientMapper.convert(entity);
    }

    @Create
    public MethodOutcome createPatient(@ResourceParam Patient thePatient) {
//        if (thePatient.getIdentifierFirstRep().isEmpty()) {
//            throw new UnprocessableEntityException("636" + "No identifier supplied");
//        }

        PatientEntity entity = patientService.create(thePatient);

        MethodOutcome retVal = new MethodOutcome();
        retVal.setId(new IdType("Patient", entity.getId()));
        return retVal;
    }
}

package owt.training.fhir.config.provider;

import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import owt.training.fhir.repository.PatientRepository;

@Component
public class PatientProvider implements IResourceProvider {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }


}

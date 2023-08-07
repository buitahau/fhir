package owt.training.fhir.util;

import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.IdType;
import owt.training.fhir.constant.PatientConstant;
import owt.training.fhir.domain.PatientEntity;
import owt.training.fhir.util.mapper.PatientMapper;

public class MethodOutcomeUtil {

    public static MethodOutcome buildMethodOutcome(PatientEntity entity) {
        return buildMethodOutcome(PatientConstant.PATIENT_RESOURCE_NAME, entity.getId(), PatientMapper.convert(entity));
    }

    private static MethodOutcome buildMethodOutcome(String resourceName, String resourceId,
                                                    DomainResource domainResource) {

        MethodOutcome retVal = new MethodOutcome();
        retVal.setId(new IdType(resourceName, resourceId));
        retVal.setResource(domainResource);
        return retVal;
    }
}

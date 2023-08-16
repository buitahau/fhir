package owt.training.fhir.auth.interceptor.evaluate.impl;

import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.r5.model.CodeableConcept;

import java.util.Set;

public class PermitUnlessDenyEvaluation extends AbstractEvaluation {

    @Override
    public PolicyEnum makeDecision(Set<CodeableConcept> allows, Set<CodeableConcept> denies) {
        if (CollectionUtils.isNotEmpty(denies)) {
            return PolicyEnum.DENY;
        }
        return PolicyEnum.ALLOW;
    }
}

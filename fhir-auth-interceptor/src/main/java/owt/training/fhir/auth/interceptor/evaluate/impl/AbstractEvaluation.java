package owt.training.fhir.auth.interceptor.evaluate.impl;

import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import org.hl7.fhir.r5.model.CodeableConcept;
import owt.training.fhir.auth.dto.wrapper.VerdictWrapper;
import owt.training.fhir.auth.interceptor.evaluate.Evaluation;

import java.util.Set;

public abstract class AbstractEvaluation implements Evaluation {

    public VerdictWrapper evaluate(Set<CodeableConcept> allows, Set<CodeableConcept> denies) {
        return new VerdictWrapper(makeDecision(allows, denies))
                .allowActions(allows)
                .denyActions(denies);
    }

    abstract PolicyEnum makeDecision(Set<CodeableConcept> allows, Set<CodeableConcept> denies);
}

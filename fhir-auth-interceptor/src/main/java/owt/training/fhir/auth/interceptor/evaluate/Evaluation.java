package owt.training.fhir.auth.interceptor.evaluate;

import org.hl7.fhir.r5.model.CodeableConcept;
import owt.training.fhir.auth.dto.wrapper.VerdictWrapper;

import java.util.Set;

public interface Evaluation {
    VerdictWrapper evaluate(Set<CodeableConcept> allows, Set<CodeableConcept> denies);
}

package owt.training.fhir.auth.interceptor.evaluate;

import org.hl7.fhir.r5.model.Permission;
import owt.training.fhir.auth.exception.FhirVaultException;
import owt.training.fhir.auth.interceptor.evaluate.impl.DenyUnlessPermitEvaluation;
import owt.training.fhir.auth.interceptor.evaluate.impl.PermitUnlessDenyEvaluation;

public class EvaluationFactory {

    private EvaluationFactory() {
    }

    public static Evaluation get(Permission.PermissionRuleCombining combiningType) {
        switch (combiningType) {
            case DENYUNLESSPERMIT:
                return new DenyUnlessPermitEvaluation();
            case PERMITUNLESSDENY:
                return new PermitUnlessDenyEvaluation();
            default:
                throw new FhirVaultException("Wrong combining type " + combiningType);
        }
    }
}

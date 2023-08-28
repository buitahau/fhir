package owt.training.fhir.auth.mapping;

import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import org.hl7.fhir.r5.model.CodeableConcept;
import owt.training.fhir.auth.constant.FhirVaultConstant;
import owt.training.fhir.auth.dto.FhirVaultDto;
import owt.training.fhir.auth.dto.wrapper.VerdictWrapper;
import owt.training.fhir.auth.enums.HttpMethodMappingEnums;

import java.util.Collection;
import java.util.List;

public class HttpMethodMappingEvaluation {

    public static AuthorizationInterceptor.Verdict evaluateHttpMapping(FhirVaultDto authorizationInterceptorDto,
                                                                       VerdictWrapper transactionalChecked, VerdictWrapper consentChecked) {

        List<String> policies = HttpMethodMappingEnums.getPolicies(authorizationInterceptorDto.getHttpMethod());

        boolean containsInTransactional = sameAtLeastOne(transactionalChecked.getAllowActions(), policies);

        boolean containsInConsent = sameAtLeastOne(consentChecked.getAllowActions(), policies);

        if (containsInTransactional || containsInConsent) {
            return new VerdictWrapper(PolicyEnum.ALLOW).buildVerdict();
        }

        return new VerdictWrapper(PolicyEnum.DENY)
                .addToDenyActions(FhirVaultConstant.NOT_MAP_HTTP_METHOD_MESSAGE)
                .buildVerdict();
    }

    private static boolean sameAtLeastOne(Collection<CodeableConcept> actions, List<String> httpMethodPolices) {
        return actions.stream()
                .anyMatch(codeableConcept -> codeableConcept.getCoding()
                        .stream()
                        .anyMatch(coding -> httpMethodPolices.contains(coding.getCode())));
    }
}

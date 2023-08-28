package owt.training.fhir.auth.mapping;

import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.hl7.fhir.r5.model.CodeableConcept;
import owt.training.fhir.auth.constant.FhirVaultConstant;
import owt.training.fhir.auth.dto.FhirVaultDto;
import owt.training.fhir.auth.dto.wrapper.VerdictWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HttpMethodMappingEvaluation {

    private Map<String, List<String>> httpMethodMapping;

    public HttpMethodMappingEvaluation(Map<String, List<String>> httpMethodMapping) {
        this.httpMethodMapping = httpMethodMapping;
    }

    public AuthorizationInterceptor.Verdict evaluateHttpMapping(FhirVaultDto authorizationInterceptorDto,
                                                                VerdictWrapper transactionalChecked,
                                                                VerdictWrapper consentChecked) {

        List<String> mappingActions = getMappingActions(authorizationInterceptorDto.getHttpMethod());
        if (CollectionUtils.isEmpty(mappingActions)) {
            return getDenyVerdict();
        }

        boolean containsInTransactional = sameAtLeastOne(transactionalChecked.getAllowActions(), mappingActions);

        boolean containsInConsent = sameAtLeastOne(consentChecked.getAllowActions(), mappingActions);

        if (containsInTransactional || containsInConsent) {
            return new VerdictWrapper(PolicyEnum.ALLOW).buildVerdict();
        }

        return getDenyVerdict();
    }

    private AuthorizationInterceptor.Verdict getDenyVerdict() {
        return new VerdictWrapper(PolicyEnum.DENY).addToDenyActions(FhirVaultConstant.NOT_MAP_HTTP_METHOD_MESSAGE)
                .buildVerdict();
    }

    private List<String> getMappingActions(String httpMethod) {
        if (MapUtils.isEmpty(httpMethodMapping) || !httpMethodMapping.containsKey(httpMethod)) {
            return List.of();
        }
        return httpMethodMapping.get(httpMethod);
    }

    private boolean sameAtLeastOne(Collection<CodeableConcept> actions, List<String> mappingActions) {
        return actions.stream()
                .anyMatch(codeableConcept -> codeableConcept.getCoding()
                        .stream()
                        .anyMatch(coding -> mappingActions.contains(coding.getCode())));
    }
}

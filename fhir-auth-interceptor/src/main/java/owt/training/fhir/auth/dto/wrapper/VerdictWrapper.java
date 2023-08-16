package owt.training.fhir.auth.dto.wrapper;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import owt.training.fhir.auth.constant.FhirVaultConstant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class VerdictWrapper {

    private final PolicyEnum theDecision;

    private Collection<CodeableConcept> allowActions;

    private Collection<CodeableConcept> denyActions;

    public VerdictWrapper(PolicyEnum theDecision) {
        Validate.notNull(theDecision);
        this.theDecision = theDecision;
    }

    public VerdictWrapper denyActions(Collection<CodeableConcept> denyActions) {
        this.denyActions = denyActions;
        return this;
    }

    public VerdictWrapper allowActions(Collection<CodeableConcept> allowActions) {
        this.allowActions = allowActions;
        return this;
    }

    public AuthorizationInterceptor.Verdict buildVerdict() {
        if (PolicyEnum.ALLOW == theDecision) {
            return new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, null);
        }

        if (CollectionUtils.isEmpty(denyActions)) {
            addToDenyActions(FhirVaultConstant.NO_ALLOW_RULES_MESSAGE);
        }

        IAuthRule iAuthRule = buildRule(denyActions.stream()
                .flatMap(codeableConcept -> codeableConcept.getCoding().stream().map(Coding::getDisplay))
                .collect(Collectors.joining(","))
        );

        return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, iAuthRule);
    }

    public VerdictWrapper addToDenyActions(String displayAction) {
        if (denyActions == null) {
            denyActions = new ArrayList<>();
        }
        denyActions.add(new CodeableConcept().addCoding(new Coding().setDisplay(displayAction)));
        return this;
    }

    protected IAuthRule buildRule(String ruleName) {
        return new IAuthRule() {
            @Override
            public AuthorizationInterceptor.Verdict applyRule(RestOperationTypeEnum restOperationTypeEnum, RequestDetails requestDetails,
                                                              IBaseResource iBaseResource, IIdType iIdType, IBaseResource iBaseResource1,
                                                              IRuleApplier iRuleApplier, Set<AuthorizationFlagsEnum> set, Pointcut pointcut) {
                return null;
            }

            @Override
            public String getName() {
                return ruleName;
            }
        };
    }

    public PolicyEnum getTheDecision() {
        return theDecision;
    }

    public Collection<CodeableConcept> getDenyActions() {
        if (CollectionUtils.isEmpty(denyActions)) {
            return Collections.emptySet();
        }
        return denyActions;
    }

    public Collection<CodeableConcept> getAllowActions() {
        if (CollectionUtils.isEmpty(allowActions)) {
            return Collections.emptySet();
        }
        return allowActions;
    }

    public boolean isDeny() {
        return PolicyEnum.DENY == theDecision;
    }
}

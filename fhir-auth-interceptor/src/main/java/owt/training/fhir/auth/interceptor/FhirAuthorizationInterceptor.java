package owt.training.fhir.auth.interceptor;

import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import owt.training.fhir.auth.dto.FhirVaultDto;
import owt.training.fhir.auth.dto.properties.FhirVaultProperties;
import owt.training.fhir.auth.dto.wrapper.ListVerdictWrapper;
import owt.training.fhir.auth.dto.wrapper.ReferenceWrapper;
import owt.training.fhir.auth.dto.wrapper.VerdictWrapper;
import owt.training.fhir.auth.interceptor.evaluate.EvaluationFactory;
import owt.training.fhir.auth.mapping.HttpMethodMappingEvaluation;
import owt.training.fhir.auth.util.FHIRFileUtil;
import owt.training.fhir.auth.util.FhirContextUtil;
import owt.training.fhir.auth.validation.PermissionValidation;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class FhirAuthorizationInterceptor extends AuthorizationInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FhirAuthorizationInterceptor.class);

    private final FhirVaultProperties properties;

    private static final IFhirPath iFhirPath;

    static {
        iFhirPath = FhirContextUtil.getiFhirPath();
    }

    public FhirAuthorizationInterceptor(FhirVaultProperties properties) {
        this.properties = properties;
    }

    @Override
    public Verdict applyRulesAndReturnDecision(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails,
                                               IBaseResource theInputResource, IIdType theInputResourceId,
                                               IBaseResource theOutputResource, Pointcut thePointcut) {

        FhirVaultDto authorizationInterceptorDto = new FhirVaultDto(theInputResourceId, theRequestDetails);

        return evaluate(authorizationInterceptorDto);
    }

    private Verdict evaluate(FhirVaultDto authorizationInterceptorDto) {
        DomainResource userLogging = authorizationInterceptorDto.buildUserLogging();

        VerdictWrapper transactionalChecked = evaluateTransactional(userLogging,
                authorizationInterceptorDto.getResourceIdentifier());

        if (transactionalChecked.isDeny()) {
            return transactionalChecked.buildVerdict();
        }

        VerdictWrapper consentChecked = evaluateConsent(userLogging,
                authorizationInterceptorDto.getResourceIdentifier());

        if (consentChecked.isDeny()) {
            return consentChecked.buildVerdict();
        }

        return new HttpMethodMappingEvaluation(properties.getHttpMethodMapping())
                .evaluateHttpMapping(authorizationInterceptorDto, transactionalChecked, consentChecked);
    }

    private VerdictWrapper evaluateConsent(DomainResource userLogging, String resourceIdentifier) {
        String fileNamePrefix = String.format(properties.getPrefixConsentFileName(), resourceIdentifier);
        return evaluatePermission(userLogging, fileNamePrefix);
    }

    private VerdictWrapper evaluateTransactional(DomainResource userLogging, String resourceIdentifier) {
        String fileNamePrefix = String.format(properties.getPrefixTransactionFileName(), resourceIdentifier);
        return evaluatePermission(userLogging, fileNamePrefix);
    }

    private VerdictWrapper evaluatePermission(DomainResource userLogging, String fileNamePrefix) {
        List<File> permissionFiles = FHIRFileUtil.findFiles(properties.getFilePath(), fileNamePrefix);
        return evaluate(userLogging, permissionFiles);
    }

    private VerdictWrapper evaluate(DomainResource userLogging, List<File> permissionFiles) {
        ListVerdictWrapper listVerdictWrapper = new ListVerdictWrapper();

        for (File file : permissionFiles) {
            Permission permission = FHIRFileUtil.readResource(file, Permission.class);
            if (!PermissionValidation.isValid(permission)) {
                continue;
            }

            Map<ReferenceWrapper, Set<CodeableConcept>> permitPoliciesOfResource = parsePermission(permission,
                    Enumerations.ConsentProvisionType.PERMIT);
            Map<ReferenceWrapper, Set<CodeableConcept>> denyPoliciesOfResource = parsePermission(permission,
                    Enumerations.ConsentProvisionType.DENY);

            Set<CodeableConcept> userLoggingAllowActions =
                    evaluateXPathExpressionAndCollectActions(userLogging, permitPoliciesOfResource);
            Set<CodeableConcept> userLoggingDenyActions =
                    evaluateXPathExpressionAndCollectActions(userLogging, denyPoliciesOfResource);

            listVerdictWrapper.addVerdictWrapper(evaluate(permission.getCombining(), userLoggingAllowActions,
                    userLoggingDenyActions));
        }

        return makeDecision(listVerdictWrapper);
    }

    private VerdictWrapper makeDecision(ListVerdictWrapper verdictWrappers) {
        if (verdictWrappers.isAllMatch(PolicyEnum.ALLOW)) {
            return new VerdictWrapper(PolicyEnum.ALLOW).allowActions(verdictWrappers.getAllowActions());
        }
        return new VerdictWrapper(PolicyEnum.DENY).denyActions(verdictWrappers.getDenyActions());
    }

    private Set<CodeableConcept> evaluateXPathExpressionAndCollectActions(DomainResource userLogin,
                                                                          Map<ReferenceWrapper, Set<CodeableConcept>> rules) {

        return rules
                .entrySet()
                .stream()
                .filter(entry -> evaluate(userLogin, entry.getKey().getReference()))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toSet());
    }

    private boolean evaluate(DomainResource userLogin, Reference reference) {
        return iFhirPath.evaluate(userLogin, reference.getReference(), BooleanType.class)
                .stream()
                .findFirst()
                .orElseThrow()
                .booleanValue();
    }

    private Map<ReferenceWrapper, Set<CodeableConcept>> parsePermission(Permission permission,
                                                                        Enumerations.ConsentProvisionType type) {

        List<Permission.RuleActivityComponent> rulesActivities = permission.getRule()
                .stream()
                .filter(ruleComponent -> ruleComponent.getType() == type)
                .flatMap(ruleComponent -> ruleComponent.getActivity().stream())
                .collect(Collectors.toList());

        Map<ReferenceWrapper, Set<CodeableConcept>> mapRules = new HashMap<>();
        for (Permission.RuleActivityComponent component : rulesActivities) {
            List<Reference> actor = component.getActor();
            Set<CodeableConcept> action = new HashSet<>(component.getAction());
            for (Reference reference : actor) {
                mapRules.put(new ReferenceWrapper(reference), action);
            }
        }
        return mapRules;
    }

    private VerdictWrapper evaluate(Permission.PermissionRuleCombining combining, Set<CodeableConcept> allows,
                                    Set<CodeableConcept> denies) {

        return EvaluationFactory.get(combining).evaluate(allows, denies);
    }
}
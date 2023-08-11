package owt.training.fhir.interceptor;

import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.*;
import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import owt.training.fhir.auth.dto.DomainResourceWrapper;
import owt.training.fhir.auth.dto.FHIRClaim;
import owt.training.fhir.auth.dto.FhirVaultProperties;
import owt.training.fhir.auth.dto.ReferenceWrapper;
import owt.training.fhir.auth.util.FHIRFileUtil;
import owt.training.fhir.auth.util.FhirContextUtil;
import owt.training.fhir.auth.util.JWTTokenUtil;

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

        // Get principal, namespaces, roles from jwt
        FHIRClaim jwtDto = JWTTokenUtil.parsingJwtToken(theRequestDetails);
        log.info("FhirAuthorizationInterceptor[JWT information:{}]", jwtDto);

        // FHIR operation
        String fhirOperation = theRequestDetails.getRequestType().name();
        log.info("FhirAuthorizationInterceptor[fhirOperation:{}]", fhirOperation);

        // FHIR resource
        String resourceName = theRequestDetails.getResourceName();
        String requestPath = theRequestDetails.getRequestPath();
        // get from body, header or url
        String resourceIdentifier = requestPath
                .replace(resourceName, "").replace("/", "");
        log.info("FhirAuthorizationInterceptor[resourceName:{}]", resourceName);
        log.info("FhirAuthorizationInterceptor[resourceIdentifier:{}]", resourceIdentifier);

        // Tenant
        String tenant = theRequestDetails.getTenantId();
        log.info("FhirAuthorizationInterceptor[tenant:{}]", tenant);

        return evaluate(jwtDto, resourceIdentifier);
    }

    private Verdict evaluate(FHIRClaim jwtDto, String resourceIdentifier) {
        DomainResource userLogging = buildUserLogin(jwtDto);

        Verdict transactionalChecked = evaluateTransactional(userLogging, resourceIdentifier);

        if (isDeny(transactionalChecked)) {
            return transactionalChecked;
        }

        return evaluateConsent(userLogging, resourceIdentifier);
    }

    private Verdict evaluateConsent(DomainResource userLogging, String resourceIdentifier) {
        String fileNamePrefix = resourceIdentifier + "_permission_consent";
        List<File> consentFiles = FHIRFileUtil.findFiles(properties.getFilePath(), fileNamePrefix);
        return evaluate(userLogging, consentFiles);
    }

    private boolean isDeny(Verdict statusTransaction) {
        return PolicyEnum.DENY.equals(statusTransaction.getDecision());
    }

    private Verdict evaluateTransactional(DomainResource userLogging, String resourceIdentifier) {
        String fileNamePrefix = resourceIdentifier + "_permission_transactional";
        List<File> transactionFiles = FHIRFileUtil.findFiles(properties.getFilePath(), fileNamePrefix);
        return evaluate(userLogging, transactionFiles);
    }

    private Verdict evaluate(DomainResource userLogging, List<File> permissionFiles) {
        Map<ReferenceWrapper, Set<CodeableConcept>> permitPoliciesOfResource = parsePermission(permissionFiles,
                Enumerations.ConsentProvisionType.PERMIT);
        Map<ReferenceWrapper, Set<CodeableConcept>> denyPoliciesOfResource = parsePermission(permissionFiles,
                Enumerations.ConsentProvisionType.DENY);

        Set<CodeableConcept> userLoggingAllowPolicies =
                evaluateXPathExpressionAndGetPolicy(userLogging, permitPoliciesOfResource);
        Set<CodeableConcept> userLoggingDenyPolicies =
                evaluateXPathExpressionAndGetPolicy(userLogging, denyPoliciesOfResource);

        return evaluate(userLoggingDenyPolicies, userLoggingAllowPolicies);
    }

    private Set<CodeableConcept> evaluateXPathExpressionAndGetPolicy(DomainResource userLogin,
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

    private DomainResource buildUserLogin(FHIRClaim jwtDto) {
        DomainResourceWrapper userLoggingWrapper = new DomainResourceWrapper(jwtDto.getResourceType())
                .addIdentifier(jwtDto.getAccountUrn())
                .addIdentifier(jwtDto.getGlnUrn())
                .addIdentifier(jwtDto.getMpildUrn())
                .addExtension("group", jwtDto.getGroups())
                .addExtension("scope", jwtDto.getScope());
        return userLoggingWrapper.getResource();
    }

    private Map<ReferenceWrapper, Set<CodeableConcept>> parsePermission(List<File> permissionFiles,
                                                                        Enumerations.ConsentProvisionType consentProvisionType) {

        Map<ReferenceWrapper, Set<CodeableConcept>> result = new HashMap<>();
        for (File file : permissionFiles) {
            Permission permission = FHIRFileUtil.readResource(file, Permission.class);
            Map<ReferenceWrapper, List<CodeableConcept>> rules = parsePermission(permission, consentProvisionType);
            for (Map.Entry<ReferenceWrapper, List<CodeableConcept>> entry : rules.entrySet()) {
                if (!result.containsKey(entry.getKey())) {
                    result.put(entry.getKey(), new HashSet<>());
                }
                result.get(entry.getKey()).addAll(entry.getValue());
            }
        }
        return result;
    }

    private Map<ReferenceWrapper, List<CodeableConcept>> parsePermission(Permission permission,
                                                                         Enumerations.ConsentProvisionType type) {

        List<Permission.RuleActivityComponent> rulesActivities = permission.getRule()
                .stream()
                .filter(ruleComponent -> ruleComponent.getType() == type)
                .flatMap(ruleComponent -> ruleComponent.getActivity().stream())
                .collect(Collectors.toList());

        Map<ReferenceWrapper, List<CodeableConcept>> mapRules = new HashMap<>();
        for (Permission.RuleActivityComponent component : rulesActivities) {
            List<Reference> actor = component.getActor();
            List<CodeableConcept> action = component.getAction();
            for (Reference reference : actor) {
                mapRules.put(new ReferenceWrapper(reference), action);
            }
        }
        return mapRules;
    }

    private Verdict evaluate(Set<CodeableConcept> denies, Set<CodeableConcept> allows) {
        if (CollectionUtils.isNotEmpty(denies)) {
            return new Verdict(PolicyEnum.DENY,
                    buildRule(denies.stream().findAny().get().getCodingFirstRep().getDisplay()));
        }

        if (CollectionUtils.isEmpty(allows)) {
            return new Verdict(PolicyEnum.DENY, buildRule("No allow rules"));
        }

        return new Verdict(PolicyEnum.ALLOW, null);
    }

    private IAuthRule buildRule(String ruleName) {
        return new IAuthRule() {
            @Override
            public Verdict applyRule(RestOperationTypeEnum restOperationTypeEnum, RequestDetails requestDetails,
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
}

package owt.training.fhir.auth.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import owt.training.fhir.auth.dto.FHIRClaim;
import owt.training.fhir.auth.dto.ReferenceWrapper;
import owt.training.fhir.auth.util.FHIRFileUtil;
import owt.training.fhir.auth.util.JWTTokenUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class FhirAuthorizationInterceptor extends AuthorizationInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FhirAuthorizationInterceptor.class);

    private final String pathFiles;

    private final IFhirPath iFhirPath;

    private final IParser iParser;

    public FhirAuthorizationInterceptor(String pathFiles) {
        this.pathFiles = pathFiles;
        FhirContext ctx = FhirContext.forR5Cached();
        iFhirPath = ctx.newFhirPath();
        iParser = ctx.newJsonParser();
    }

    @Override
    public Verdict applyRulesAndReturnDecision(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, Pointcut thePointcut) {
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
        DomainResource userLogin = buildUserLogin(jwtDto);

        String fileNamePrefix = resourceIdentifier + "_permission_transactional";
        List<File> transactionFiles = FHIRFileUtil.findFiles(pathFiles, fileNamePrefix);

        Map<ReferenceWrapper, Set<CodeableConcept>> permitRules = parseRulesFromPermissions(transactionFiles,
                Enumerations.ConsentProvisionType.PERMIT);
        Map<ReferenceWrapper, Set<CodeableConcept>> denyRules = parseRulesFromPermissions(transactionFiles,
                Enumerations.ConsentProvisionType.DENY);

        Set<CodeableConcept> deny = compareConditionAndGetPolicy(userLogin, denyRules);
        Set<CodeableConcept> allow = compareConditionAndGetPolicy(userLogin, permitRules);

        return evaluate(deny, allow);
    }

    private Set<CodeableConcept> compareConditionAndGetPolicy(DomainResource userLogin,
                                                              Map<ReferenceWrapper, Set<CodeableConcept>> rules) {

        Set<CodeableConcept> result = new HashSet<>();
        for (Map.Entry<ReferenceWrapper, Set<CodeableConcept>> entry : rules.entrySet()) {
            if (evaluate(userLogin, entry.getKey().getReference())) {
                result.addAll(entry.getValue());
            }
        }
        return result;
    }

    private boolean evaluate(DomainResource userLogin, Reference reference) {
        return iFhirPath.evaluate(userLogin, reference.getReference(), BooleanType.class)
                .stream()
                .findFirst()
                .orElseThrow()
                .booleanValue();
    }

    private DomainResource buildUserLogin(FHIRClaim jwtDto) {
        Person person = new Person();

        addIdentifier(person, "accountUrn", jwtDto.getAccountUrn());
        addIdentifier(person, "glnUrn", jwtDto.getGlnUrn());
        addIdentifier(person, "mpildUrn", jwtDto.getMpildUrn());

        addExtension(person, "group", jwtDto.getGroups());
        addExtension(person, "scope", jwtDto.getScope());

        return person;
    }

    private void addIdentifier(Person person, String systemIdentifier, String valueIdentifier) {
        Identifier identifier = new Identifier();
        identifier.setSystem(systemIdentifier);
        identifier.setValue(valueIdentifier);

        if (person.getIdentifier() == null) {
            person.setIdentifier(new ArrayList<>());
        }

        person.getIdentifier().add(identifier);
    }

    private void addExtension(Person person, String extensionCode, List<String> extensionValue) {
        Extension extension = new Extension();
        extension.setId(extensionCode);

        CodeableConcept codeableConcept = new CodeableConcept();
        extension.setValue(codeableConcept);

        for (String value : extensionValue) {
            Coding coding = new Coding();
            coding.setCode(value);
            codeableConcept.addCoding(coding);
        }

        person.addExtension(extension);
    }

    private Map<ReferenceWrapper, Set<CodeableConcept>> parseRulesFromPermissions(List<File> permissionFiles,
                                                                                  Enumerations.ConsentProvisionType consentProvisionType) {

        Map<ReferenceWrapper, Set<CodeableConcept>> result = new HashMap<>();
        for (File file : permissionFiles) {
            Permission permissionTransaction = FHIRFileUtil.readResource(file, Permission.class);
            Map<ReferenceWrapper, List<CodeableConcept>> rules = parsePermission(permissionTransaction, consentProvisionType);
            for (Map.Entry<ReferenceWrapper, List<CodeableConcept>> entry : rules.entrySet()) {
                if (!result.containsKey(entry.getKey())) {
                    result.put(entry.getKey(), new HashSet<>(entry.getValue()));
                } else {
                    result.get(entry.getKey()).addAll(entry.getValue());
                }
            }
        }
        return result;
    }

    private Map<ReferenceWrapper, List<CodeableConcept>> parsePermission(Permission permission,
                                                                         Enumerations.ConsentProvisionType type) {

        List<Permission.RuleActivityComponent> permitActivities = permission.getRule()
                .stream()
                .filter(ruleComponent -> ruleComponent.getType() == type)
                .flatMap(ruleComponent -> ruleComponent.getActivity().stream())
                .collect(Collectors.toList());

        Map<ReferenceWrapper, List<CodeableConcept>> mapRules = new HashMap<>();
        for (Permission.RuleActivityComponent component : permitActivities) {
            List<Reference> actor = component.getActor();
            List<CodeableConcept> action = component.getAction();
            for (Reference reference : actor) {
                mapRules.put(new ReferenceWrapper(reference), action);
            }
        }
        return mapRules;
    }

    private Verdict evaluate(Set<CodeableConcept> deny, Set<CodeableConcept> allow) {
        if (CollectionUtils.isEmpty(deny) && CollectionUtils.isNotEmpty(allow)) {
            return new Verdict(PolicyEnum.ALLOW, null);
        }
        return new Verdict(PolicyEnum.DENY, null);
    }
}

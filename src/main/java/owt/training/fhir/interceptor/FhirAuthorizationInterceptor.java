package owt.training.fhir.interceptor;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import org.hl7.fhir.r5.model.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import owt.training.fhir.interceptor.dto.FHIRClaim;
import owt.training.fhir.util.FHIRFileUtil;
import owt.training.fhir.util.JWTTokenUtil;

import java.util.List;

public class FhirAuthorizationInterceptor extends AuthorizationInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FhirAuthorizationInterceptor.class);

    private final String pathFiles;

    public FhirAuthorizationInterceptor(String pathFiles) {
        this.pathFiles = pathFiles;
    }

    @Override
    public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
        // Get principal, namespaces, roles from jwt
        FHIRClaim jwtDto = JWTTokenUtil.parsingJwtToken(theRequestDetails);
        log.info("FhirAuthorizationInterceptor[JWT information:{}]", jwtDto);

        // FHIR operation
        String fhirOperation = theRequestDetails.getRequestType().name();
        log.info("FhirAuthorizationInterceptor[fhirOperation:{}]", fhirOperation);

        // FHIR resource
        String resourceName = theRequestDetails.getResourceName();
        String resourceIdentifier = theRequestDetails.getRequestPath();
        log.info("FhirAuthorizationInterceptor[resourceName:{}]", resourceName);
        log.info("FhirAuthorizationInterceptor[resourceIdentifier:{}]", resourceIdentifier);

        // Tenant
        String tenant = theRequestDetails.getTenantId();
        log.info("FhirAuthorizationInterceptor[tenant:{}]", tenant);

        // Read the permission
        Permission permissionConsent =
                FHIRFileUtil.readResource(pathFiles + "/permission_constent.json", Permission.class);
        Permission permissionTransaction =
                FHIRFileUtil.readResource(pathFiles + "/permission_transactional_a.json", Permission.class);

        if (jwtDto.isCara()) {
            return buildRuleList(permissionTransaction);
        }

        return buildRuleList(permissionConsent);
    }

    private List<IAuthRule> buildRuleList(Permission permissionConsent) {
        // TODO
        return null;
    }
}

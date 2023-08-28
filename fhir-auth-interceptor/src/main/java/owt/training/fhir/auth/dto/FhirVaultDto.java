package owt.training.fhir.auth.dto;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r5.model.DomainResource;
import owt.training.fhir.auth.dto.claim.FHIRClaim;
import owt.training.fhir.auth.dto.wrapper.DomainResourceWrapper;
import owt.training.fhir.auth.util.JWTTokenUtil;

public class FhirVaultDto {

    private FHIRClaim claim;

    private String resourceIdentifier;

    private RequestDetails theRequestDetails;

    public FhirVaultDto(RequestDetails theRequestDetails) {
        Validate.notNull(theRequestDetails);
        this.theRequestDetails = theRequestDetails;
    }

    public void getClaim() {
        if (claim != null) {
            return;
        }

        claim = JWTTokenUtil.parsingJwtToken(theRequestDetails);
    }

    public String getResourceIdentifier() {
        if (resourceIdentifier != null) {
            return resourceIdentifier;
        }

        resourceIdentifier = getResourceIdentifier(theRequestDetails);
        return resourceIdentifier;
    }

    private String getResourceIdentifier(RequestDetails theRequestDetails) {
        // TODO Get from url or body
        String resourceName = theRequestDetails.getResourceName();
        String requestPath = theRequestDetails.getRequestPath();
        return requestPath.replace(resourceName, "").replace("/", "");
    }

    public DomainResource buildUserLogging() {
        getClaim();
        DomainResourceWrapper userLoggingWrapper = new DomainResourceWrapper(claim.getResourceType())
                .addIdentifier(claim.getAccountUrn())
                .addIdentifier(claim.getGlnUrn())
                .addIdentifier(claim.getMpildUrn())
                .addExtension("group", claim.getGroups())
                .addExtension("scope", claim.getScope());
        return userLoggingWrapper.getResource();
    }

    public String getHttpMethod() {
        return theRequestDetails.getRequestType().name();
    }
}

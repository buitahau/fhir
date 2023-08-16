package owt.training.fhir.auth.util;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import owt.training.fhir.auth.dto.claim.FHIRClaim;
import owt.training.fhir.auth.exception.FhirVaultException;

import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JWTTokenUtil {

    public static FHIRClaim parsingJwtToken(RequestDetails requestDetails) {
        try {
            String jwtToken = getToken(requestDetails);
            JWTClaimsSet jwtClaimsSet = getJWTClaimSet(jwtToken);
            return getFhirClaim(jwtClaimsSet);
        } catch (ParseException e) {
            throw new FhirVaultException("Error in parsing jwt", e);
        }
    }

    private static FHIRClaim getFhirClaim(JWTClaimsSet jwtClaimsSet) throws ParseException {
        FHIRClaim jwtDto = new FHIRClaim();
        jwtDto.setAccountUrn(jwtClaimsSet.getStringClaim("accountUrn"));
        jwtDto.setMpildUrn(jwtClaimsSet.getStringClaim("mpildUrn"));
        jwtDto.setGlnUrn(jwtClaimsSet.getStringClaim("glnUrn"));
        jwtDto.setGroups(jwtClaimsSet.getStringListClaim("groups"));
        jwtDto.setScope(Arrays.stream(jwtClaimsSet.getStringClaim("fhirscope").split(" "))
                .collect(Collectors.toList()));
        jwtDto.setResourceType(jwtClaimsSet.getStringClaim("resourceType"));
        return jwtDto;
    }

    private static JWTClaimsSet getJWTClaimSet(String jwtToken) throws ParseException {
        JWT jwt = JWTParser.parse(jwtToken);
        return jwt.getJWTClaimsSet();
    }

    private static String getToken(RequestDetails theRequestDetails) {
        return theRequestDetails.getHeader("Authorization").replace("Bearer ", "");
    }
}

package owt.training.fhir.security.claims;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class FhirClaims {

    private Claims claims;

    private FhirClaims() {}

    public static FhirClaims defaultClaims() {
        return fromClaims(Jwts.claims());
    }

    public static FhirClaims fromClaims(Claims claims) {
        FhirClaims fhirClaims = new FhirClaims();
        fhirClaims.setClaims(claims);
        return fhirClaims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    public Claims getClaims() {
        return claims;
    }
}

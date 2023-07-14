package owt.training.fhir.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import owt.training.fhir.security.claims.FhirClaims;

import java.util.Collection;

public class FhirAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final FhirClaims fhirClaims;

    public FhirAuthenticationToken(String token, FhirClaims fhirClaims,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.fhirClaims = fhirClaims;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.fhirClaims;
    }
}

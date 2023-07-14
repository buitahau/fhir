package owt.training.fhir.security;

import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import owt.training.fhir.security.claims.FhirClaims;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class JwtTokenProvider implements Serializable {

    private static final long serialVersionUID = -1742145644909985492L;

    private static final String AUTHORITIES_KEY = "authorities";

    private static final String DELIMITER_COMMA = ",";

    private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final long tokenValidity;

    private final String secret;

    public JwtTokenProvider(String secret, long tokenValidity) {
        this.secret = secret;
        this.tokenValidity = tokenValidity;
    }

    public String generateToken(Authentication authentication) {
        FhirClaims fhirClaims = FhirClaims.defaultClaims();
        return generateToken(fhirClaims, authentication);
    }

    public Optional<Authentication> getAuthentication(String jwt) {
        Optional<FhirClaims> optionalFhirClaims = extractJwt(jwt);
        if (optionalFhirClaims.isEmpty()) {
            return Optional.empty();
        }
        FhirClaims fhirClaims = optionalFhirClaims.get();
        List<SimpleGrantedAuthority> authorities = buildAuthorities(fhirClaims);
        return Optional.of(new FhirAuthenticationToken(jwt, fhirClaims, authorities));
    }

    private List<SimpleGrantedAuthority> buildAuthorities(FhirClaims fhirClaims) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(fhirClaims.getClaims().get(AUTHORITIES_KEY).toString().split(DELIMITER_COMMA))
                .filter(StringUtils::isNotBlank)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);
        authorities.add(new SimpleGrantedAuthority("AUTHENTICATED"));
        return authorities;
    }

    private String generateToken(FhirClaims claims, Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(DELIMITER_COMMA));

        return Jwts.builder()
                .setClaims(claims.getClaims())
                .claim(AUTHORITIES_KEY, authorities)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Optional<FhirClaims> extractJwt(String jwt) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody();
            return Optional.of(FhirClaims.fromClaims(claims));
        } catch (ExpiredJwtException e) {
            log.info("Token expired");
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
        }
        return Optional.empty();
    }
}

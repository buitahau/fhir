package owt.training.fhir.config.security.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakJwtTokenConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private static final String CLAIM_RESOURCE_ACCESS = "resource_access";

    private static final String ROLE_PREFIX = "ROLE_";

    private static final String CLAIM_ROLES = "roles";

    private TokenConverterProperties tokenConverterProperties;

    public KeycloakJwtTokenConverter(TokenConverterProperties tokenConverterProperties) {
        this.tokenConverterProperties = tokenConverterProperties;
    }

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        Stream<SimpleGrantedAuthority> accesses = Stream.of(jwt)
                .map(token -> token.getClaimAsMap(CLAIM_RESOURCE_ACCESS))
                .map(claimMap -> (Map<String, Object>) claimMap.get(tokenConverterProperties.getResourceId()))
                .map(resourceData -> (Collection<String>) resourceData.get(CLAIM_ROLES))
                .flatMap(Collection::stream)
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .distinct();

        Set<GrantedAuthority> authorities =
                Stream.concat(new JwtGrantedAuthoritiesConverter().convert(jwt).stream(), accesses)
                        .collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities);
    }
}

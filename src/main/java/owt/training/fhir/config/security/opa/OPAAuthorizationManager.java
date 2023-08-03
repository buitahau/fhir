package owt.training.fhir.config.security.opa;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import owt.training.fhir.config.security.opa.cachedservlet.ContentCachingHttpServletRequest;
import owt.training.fhir.config.security.opa.dto.OPADataRequest;
import owt.training.fhir.config.security.opa.dto.OPADataResponse;
import owt.training.fhir.config.security.opa.dto.OPAInput;
import owt.training.fhir.config.security.opa.proxy.rest.OPAClient;

import java.io.IOException;
import java.util.function.Supplier;

@Component
public class OPAAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private OPAClient opaClient;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext requestAuthorizationContext) {

        var httpServletRequest = requestAuthorizationContext.getRequest();

        OPAInput opaInput = OPAInput.builder()
                .jwt(getToken(authentication.get()))
                .method(httpServletRequest.getMethod())
                .authorities(authentication.get().getAuthorities().stream().toList())
                .payload(getPayload(httpServletRequest))
                .uri(getUri(httpServletRequest))
                .build();

        OPADataResponse opaDataResponse = opaClient.authorizedToAccessAPI(new OPADataRequest(opaInput));

        return new AuthorizationDecision(opaDataResponse.getResult().getAuthorized());
    }

    private String getToken(Authentication authentication) {
        return ((Jwt) authentication.getCredentials()).getTokenValue();
    }

    private String getUri(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRequestURI().replace(httpServletRequest.getContextPath(), "");
    }

    private Object getPayload(HttpServletRequest httpServletRequest) {
        ContentCachingHttpServletRequest contentCachingHttpServletRequest =
                WebUtils.getNativeRequest(httpServletRequest, ContentCachingHttpServletRequest.class);
        try {
            return objectMapper.readTree(contentCachingHttpServletRequest.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

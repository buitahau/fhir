package owt.training.opa.config.security.opa;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import owt.training.opa.config.security.opa.cachedservlet.ContentCachingHttpServletRequest;
import owt.training.opa.proxy.rest.OPAClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class OPAAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private OPAClient opaClient;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
        var httpServletRequest = requestAuthorizationContext.getRequest();

        String[] path = httpServletRequest.getRequestURI().replaceAll("^/|/$", "").split("/");

        ContentCachingHttpServletRequest contentCachingHttpServletRequest = WebUtils.getNativeRequest(httpServletRequest, ContentCachingHttpServletRequest.class);

        Map<String, Object> input = new HashMap<>();

        input.put("user", authentication.get().getPrincipal());
        input.put("method", httpServletRequest.getMethod());
        input.put("path", path);
        try {
            input.put("payload", objectMapper.readTree(contentCachingHttpServletRequest.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OPADataResponse opaDataResponse = opaClient.authorizedToAccessAPI(new OPADataRequest(input));

        return new AuthorizationDecision(opaDataResponse.getResult().getAllow());
    }
}

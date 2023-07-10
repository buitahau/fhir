package owt.training.fhir.config;

import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import owt.training.fhir.config.interceptor.CustomLoggingInterceptor;
import owt.training.fhir.config.provider.MockPatientProvider;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class FhirRestfulServer extends RestfulServer {

    private static final long serialVersionUID = 1L;

    @Override
    public void initialize() {

        /*
         Add Resource Providers
         */
        List<IResourceProvider> providers = new ArrayList<>();
        providers.add(new MockPatientProvider());
        setResourceProviders(providers);

        /*
        Add Interceptors
         */
        registerInterceptor(new CustomLoggingInterceptor()); // your logging
        registerInterceptor(new ResponseHighlighterInterceptor()); // enable viewing in browser

        /*
        Various config
         */
        setDefaultResponseEncoding(EncodingEnum.JSON);
        setImplementationDescription("Spring Boot HAPI-FHIR (R4) Simple Server");
        setDefaultPrettyPrint(true);

    }
}


package owt.training.fhir.config.server;

import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import owt.training.fhir.interceptor.CustomLoggingInterceptor;
import owt.training.fhir.provider.PatientProvider;
import owt.training.fhir.provider.PractitionerProvider;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class FhirRestfulServer extends RestfulServer {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PatientProvider patientProvider;

    @Autowired
    private PractitionerProvider practitionerProvider;

    @Override
    public void initialize() {
        addResourceProviders();
        registerInterceptors();
        setVariousConfig();
    }

    private void addResourceProviders() {
        List<IResourceProvider> providers = new ArrayList<>();
//        providers.add(new MockPatientProvider());
        providers.add(patientProvider);
        providers.add(practitionerProvider);
        setResourceProviders(providers);
    }

    private void registerInterceptors() {
        registerInterceptor(new CustomLoggingInterceptor()); // your logging
        registerInterceptor(new ResponseHighlighterInterceptor()); // enable viewing in browser
    }

    private void setVariousConfig() {
        setDefaultResponseEncoding(EncodingEnum.JSON);
        setImplementationDescription("Spring Boot HAPI-FHIR (R4) Simple Server");
        setDefaultPrettyPrint(true);
    }
}


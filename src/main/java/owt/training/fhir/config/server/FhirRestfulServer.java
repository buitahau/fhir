package owt.training.fhir.config.server;

import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
//import owt.training.fhir.auth.interceptor.FhirAuthorizationInterceptor;
import owt.training.fhir.interceptor.CustomLoggingInterceptor;
import owt.training.fhir.interceptor.FhirAuthorizationInterceptor;
import owt.training.fhir.provider.PatientProvider;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/hapi/*", loadOnStartup = 1)
public class FhirRestfulServer extends RestfulServer {

    private static final long serialVersionUID = 1L;

    private static final String PATH_FILES = "/home/owt-hau/work/projects/fhir/08CARA-XXXX/fhir_resources";

    @Autowired
    private PatientProvider patientProvider;

    @Override
    public void initialize() {
        addResourceProviders();
        registerInterceptors();
        setVariousConfig();
    }

    private void addResourceProviders() {
        List<IResourceProvider> providers = new ArrayList<>();
        providers.add(patientProvider);
        setResourceProviders(providers);
    }

    private void registerInterceptors() {
        registerInterceptor(new CustomLoggingInterceptor());
        registerInterceptor(new ResponseHighlighterInterceptor());
        registerInterceptor(new FhirAuthorizationInterceptor(PATH_FILES));
    }

    private void setVariousConfig() {
        setDefaultResponseEncoding(EncodingEnum.JSON);
        setImplementationDescription("Spring Boot HAPI-FHIR (R4) Simple Server");
        setDefaultPrettyPrint(true);
    }
}


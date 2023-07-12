package owt.training.fhir.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"owt.training.fhir"})
public class ApplicationConfiguration extends SpringBootServletInitializer {

    private static final Class<ApplicationConfiguration> applicationClass = ApplicationConfiguration.class;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }
}

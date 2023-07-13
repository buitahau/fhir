package owt.training.fhir.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@ComponentScan({"owt.training.fhir"})
@EnableJpaAuditing
public class ApplicationConfiguration extends SpringBootServletInitializer {

    private static final Class<ApplicationConfiguration> applicationClass = ApplicationConfiguration.class;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }
}

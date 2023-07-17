package owt.training.fhir.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TimedJobs extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:search-all-patients?delay={{application.camel.delay}}&period={{application.camel.period}}")
                .routeId("search-all-patients")
                .bean("patientServiceImpl", "findAll()")
                .split(body())
                .log("Patient[name:${body.getName}, telecom:${body.getTelecom}]");
    }
}

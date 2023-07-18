package owt.training.fhir.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TimedJobs extends RouteBuilder {

    @Override
    public void configure() throws Exception {
//        timerEx1();
        timerSendToQueue();
        timerReadFromQueue();
    }

    private void timerReadFromQueue() {
        from("jms:queue:HELLO.WORLD")
                .log("Received a message - ${body}");
    }

    private void timerSendToQueue() {
        from("timer:mytimer?period=5000")
                .setBody(constant("HELLO from Camel!"))
                .to("jms:queue:HELLO.WORLD");
    }

    private void timerEx1() {
        from("timer:search-all-patients?delay={{application.camel.delay}}&period={{application.camel.period}}")
                .routeId("search-all-patients")
                .bean("patientServiceImpl", "findAll()")
                .split(body())
                .log("Patient[name:${body.getName}, telecom:${body.getTelecom}]");
    }
}

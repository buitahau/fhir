package owt.training.fhir.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import owt.training.fhir.domain.PatientEntity;

import java.util.List;
import java.util.Optional;

@Component
public class RestRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration().component("servlet").bindingMode(RestBindingMode.auto);

        rest()
                .path("/api/patient")
                .consumes("application/json")
                .produces("application/json")
                // HTTP: GET all
                .get()
                .outType(List.class)
                .to("bean:patientServiceImpl?method=findAll()")
                // HTTP: GET
                .get("{id}")
                .outType(Optional.class)
                .to("bean:patientServiceImpl?method=findById(${header.id})")
                // HTTP: POST
                .post()
                .type(PatientEntity.class)
                .outType(PatientEntity.class)
                .to("bean:patientServiceImpl?method=create(${body})")
                // HTTP: PUT
                .put("{id}")
                .type(PatientEntity.class)
                .outType(PatientEntity.class)
                .to("bean:patientServiceImpl?method=update(${header.id}, ${body})");
    }
}

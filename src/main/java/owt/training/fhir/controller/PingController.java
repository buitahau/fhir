package owt.training.fhir.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/Patient/{id}")
    public String getPatient(@PathVariable("id") String id) {
        return "Get detail of patient " + id;
    }

    @GetMapping("/Practitioner/{id}")
    public String getPractitioner(@PathVariable("id") String id) {
        return "Get detail of practitioner " + id;
    }
}

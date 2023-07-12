package owt.training.fhir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class FhirApplication {

    public static void main(String[] args) {
        SpringApplication.run(FhirApplication.class, args);
    }
}

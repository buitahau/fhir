package owt.training.fhir.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.SchemaBaseValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirConfiguration {

    @Bean
    public FhirValidator fhirValidator() {
        FhirContext fhirContext = FhirContext.forR4();
        FhirValidator validator = fhirContext.newValidator();

        // Create a validation module and register it
        IValidatorModule module = new SchemaBaseValidator(fhirContext);
        validator.registerValidatorModule(module);

        return validator;
    }
}

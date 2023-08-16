package owt.training.fhir.auth.exception;

public class FhirVaultException extends RuntimeException {

    public FhirVaultException() {
        super();
    }

    public FhirVaultException(String message) {
        super(message);
    }

    public FhirVaultException(String message, Throwable cause) {
        super(message, cause);
    }
}

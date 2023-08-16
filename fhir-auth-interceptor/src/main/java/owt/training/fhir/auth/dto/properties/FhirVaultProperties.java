package owt.training.fhir.auth.dto.properties;

public class FhirVaultProperties {

    private final String filePath;

    private final String prefixTransactionFileName;

    private final String prefixConsentFileName;

    public FhirVaultProperties(String filePath, String prefixTransactionFileName, String prefixConsentFileName) {
        this.filePath = filePath;
        this.prefixTransactionFileName = prefixTransactionFileName;
        this.prefixConsentFileName = prefixConsentFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getPrefixConsentFileName() {
        return prefixConsentFileName;
    }

    public String getPrefixTransactionFileName() {
        return prefixTransactionFileName;
    }
}

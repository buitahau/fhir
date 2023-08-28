package owt.training.fhir.auth.dto.properties;

import java.util.List;
import java.util.Map;

public class FhirVaultProperties {

    private String filePath;

    private String prefixTransactionFileName;

    private String prefixConsentFileName;

    private Map<String, List<String>> httpMethodMapping;

    public FhirVaultProperties() {}

    public FhirVaultProperties(String filePath, String prefixTransactionFileName, String prefixConsentFileName) {
        this.filePath = filePath;
        this.prefixTransactionFileName = prefixTransactionFileName;
        this.prefixConsentFileName = prefixConsentFileName;
    }

    public FhirVaultProperties(String filePath, String prefixTransactionFileName, String prefixConsentFileName,
                               Map<String, List<String>> httpMethodMapping) {

        this.filePath = filePath;
        this.prefixTransactionFileName = prefixTransactionFileName;
        this.prefixConsentFileName = prefixConsentFileName;
        this.httpMethodMapping = httpMethodMapping;
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

    public Map<String, List<String>> getHttpMethodMapping() {
        return httpMethodMapping;
    }

    public FhirVaultProperties setHttpMethodMapping(Map<String, List<String>> httpMethodMapping) {
        this.httpMethodMapping = httpMethodMapping;
        return this;
    }

    public FhirVaultProperties setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public FhirVaultProperties setPrefixConsentFileName(String prefixConsentFileName) {
        this.prefixConsentFileName = prefixConsentFileName;
        return this;
    }

    public FhirVaultProperties setPrefixTransactionFileName(String prefixTransactionFileName) {
        this.prefixTransactionFileName = prefixTransactionFileName;
        return this;
    }
}

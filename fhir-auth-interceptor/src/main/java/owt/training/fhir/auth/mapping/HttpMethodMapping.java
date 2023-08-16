package owt.training.fhir.auth.mapping;

import owt.training.fhir.auth.exception.FhirVaultException;

import java.util.List;

public enum HttpMethodMapping {
    GET("GET", List.of("read", "vread", "search")),
    POST("POST", List.of("create"));

    private String httpMethodName;

    private List<String> policies;

    HttpMethodMapping(String httpMethodName, List<String> policies) {
        this.httpMethodName = httpMethodName;
        this.policies = policies;
    }

    public static List<String> getPolicies(String httpMethodName) {
        for(HttpMethodMapping httpMethodMapping : HttpMethodMapping.values()) {
            if (httpMethodMapping.httpMethodName.equals(httpMethodName)) {
                return httpMethodMapping.policies;
            }
        }
        throw new FhirVaultException("Not found mapping with " + httpMethodName);
    }
}

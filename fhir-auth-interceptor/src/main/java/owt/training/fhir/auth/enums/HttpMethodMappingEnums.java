package owt.training.fhir.auth.enums;

import owt.training.fhir.auth.exception.FhirVaultException;

import java.util.List;

public enum HttpMethodMappingEnums {
    GET("GET", List.of("read", "vread", "search")),
    POST("POST", List.of("create"));

    private String httpMethodName;

    private List<String> policies;

    HttpMethodMappingEnums(String httpMethodName, List<String> policies) {
        this.httpMethodName = httpMethodName;
        this.policies = policies;
    }

    public static List<String> getPolicies(String httpMethodName) {
        for (HttpMethodMappingEnums httpMethodMapping : HttpMethodMappingEnums.values()) {
            if (httpMethodMapping.httpMethodName.equals(httpMethodName)) {
                return httpMethodMapping.policies;
            }
        }
        throw new FhirVaultException("Not found mapping with " + httpMethodName);
    }
}

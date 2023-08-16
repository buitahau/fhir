package owt.training.fhir.auth.enums;

import java.util.List;

public enum ClaimEnums {
    ACCOUNT_URN("accountUrn", true, String.class),
    MPILD_URN("mpildUrn", true, String.class),
    GLN_URN("glnUrn", true, String.class),
    GROUP("groups", false, List.class),
    SCOPE("fhirscope", false, String.class);

    private String name;

    private boolean isIdentifier;

    private Class clazz;

    ClaimEnums(String name, boolean isIdentifier, Class clazz) {
        this.name = name;
        this.isIdentifier = isIdentifier;
    }
}

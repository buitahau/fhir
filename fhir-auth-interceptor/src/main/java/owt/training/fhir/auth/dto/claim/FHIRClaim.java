package owt.training.fhir.auth.dto.claim;

import java.util.List;

public class FHIRClaim {

    private String accountUrn;

    private String glnUrn;

    private String mpildUrn;

    private List<String> groups;

    private List<String> scope;

    private String resourceType;

    public String getAccountUrn() {
        return accountUrn;
    }

    public void setAccountUrn(String accountUrn) {
        this.accountUrn = accountUrn;
    }

    public String getGlnUrn() {
        return glnUrn;
    }

    public void setGlnUrn(String glnUrn) {
        this.glnUrn = glnUrn;
    }

    public String getMpildUrn() {
        return mpildUrn;
    }

    public void setMpildUrn(String mpildUrn) {
        this.mpildUrn = mpildUrn;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public String toString() {
        return "FHIRClaim{" +
                "accountUrn='" + accountUrn + '\'' +
                ", glnUrn='" + glnUrn + '\'' +
                ", mpildUrn='" + mpildUrn + '\'' +
                ", groups=" + groups +
                ", scope=" + scope +
                ", resourceType=" + resourceType +
                '}';
    }
}
package owt.training.fhir.interceptor.dto;

import java.util.List;

public class FHIRClaim {

    private String accountUrn;

    private String glnUrn;

    private String mpildUrn;

    private List<String> groups;

    private List<String> scope;

    private Boolean isCara;

    private String resourceName;

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

    public boolean isCara() {
        if (isCara == null) {
            return false;
        }
        return isCara;
    }

    public void setCara(Boolean cara) {
        isCara = cara;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        return "FHIRClaim{" +
                "accountUrn='" + accountUrn + '\'' +
                ", glnUrn='" + glnUrn + '\'' +
                ", mpildUrn='" + mpildUrn + '\'' +
                ", groups=" + groups +
                ", scope=" + scope +
                ", isCara=" + isCara +
                '}';
    }
}

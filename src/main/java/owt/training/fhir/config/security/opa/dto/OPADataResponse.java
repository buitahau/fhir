package owt.training.fhir.config.security.opa.dto;

public class OPADataResponse {

    private OPAResult result;

    public OPAResult getResult() {
        return result;
    }

    public void setResult(OPAResult result) {
        this.result = result;
    }

    public static class OPAResult {
        private String[] allow;

        private Boolean authorized;

        public Boolean getAuthorized() {
            return authorized;
        }

        public void setAuthorized(Boolean authorized) {
            this.authorized = authorized;
        }

        public String[] getAllow() {
            return allow;
        }

        public void setAllow(String[] allow) {
            this.allow = allow;
        }
    }
}

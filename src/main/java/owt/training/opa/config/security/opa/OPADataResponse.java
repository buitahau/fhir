package owt.training.opa.config.security.opa;

public class OPADataResponse {

    private OPAResult result;

    public OPAResult getResult() {
        return result;
    }

    public void setResult(OPAResult result) {
        this.result = result;
    }

    public static class OPAResult {
        private Boolean allow;

        public Boolean getAllow() {
            return allow;
        }

        public void setAllow(Boolean allow) {
            this.allow = allow;
        }
    }
}

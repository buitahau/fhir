package owt.training.opa.config.security.opa;

import java.util.Map;

public class OPADataRequest {

    private Map<String, Object> input;

    public OPADataRequest() {}

    public OPADataRequest(Map<String, Object> input) {
        this.input = input;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }
}

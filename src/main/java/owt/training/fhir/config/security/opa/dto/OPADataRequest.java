package owt.training.fhir.config.security.opa.dto;

public class OPADataRequest {

    private OPAInput input;

    public OPADataRequest() {}

    public OPADataRequest(OPAInput input) {
        this.input = input;
    }

    public OPAInput getInput() {
        return input;
    }

    public void setInput(OPAInput input) {
        this.input = input;
    }
}

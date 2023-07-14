package owt.training.fhir.security.dto;

import java.io.Serializable;

public class JwtResponseDto implements Serializable {

    private static final long serialVersionUID = 7536682276568320779L;

    private final String jwttoken;

    public JwtResponseDto(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return this.jwttoken;
    }
}

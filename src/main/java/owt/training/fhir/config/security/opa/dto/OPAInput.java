package owt.training.fhir.config.security.opa.dto;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;

public class OPAInput {
    private String jwt;
    private String method;
    private String uri;
    private List<? extends GrantedAuthority> authorities;
    private Object payload;

    public static OPAInputBuilder builder() {
        return new OPAInputBuilder();
    }

    public OPAInput(final String jwt, final String method, final String uri, final List<? extends GrantedAuthority> authorities, Object payload) {
        this.jwt = jwt;
        this.method = method;
        this.uri = uri;
        this.authorities = authorities;
        this.payload = payload;
    }

    public OPAInput() {
    }

    public String getJwt() {
        return this.jwt;
    }

    public String getMethod() {
        return this.method;
    }

    public String getUri() {
        return this.uri;
    }

    public List<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setJwt(final String jwt) {
        this.jwt = jwt;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public void setAuthorities(final List<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public static class OPAInputBuilder {
        private String jwt;
        private String method;
        private String uri;
        private List<? extends GrantedAuthority> authorities;
        private Object payload;

        OPAInputBuilder() {
        }

        public OPAInputBuilder jwt(final String jwt) {
            this.jwt = jwt;
            return this;
        }

        public OPAInputBuilder method(final String method) {
            this.method = method;
            return this;
        }

        public OPAInputBuilder uri(final String uri) {
            this.uri = uri;
            return this;
        }

        public OPAInputBuilder authorities(final List<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public OPAInputBuilder payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public OPAInput build() {
            return new OPAInput(this.jwt, this.method, this.uri, this.authorities, this.payload);
        }

        public String toString() {
            return "OPAInput.OPAInputBuilder(jwt=" + this.jwt + ", method=" + this.method + ", uri=" + this.uri + ", authorities=" + this.authorities + ")";
        }
    }
}

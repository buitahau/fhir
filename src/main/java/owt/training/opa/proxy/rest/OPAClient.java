package owt.training.opa.proxy.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import owt.training.opa.config.security.opa.OPADataRequest;
import owt.training.opa.config.security.opa.OPADataResponse;

@FeignClient(value = "opaAuthorization", url = "${application.opa.authz.url}")
public interface OPAClient {

    @PostMapping("/jcompetence/authz")
    OPADataResponse authorizedToAccessAPI(@RequestBody OPADataRequest opaDataRequest);
}

package owt.training.fhir.config.security.opa.proxy.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import owt.training.fhir.config.security.opa.dto.OPADataRequest;
import owt.training.fhir.config.security.opa.dto.OPADataResponse;

@FeignClient(value = "opaAuthorization", url = "${application.opa.authz.url}")
public interface OPAClient {

    @PostMapping("/owt/training/fhir/authz")
    OPADataResponse authorizedToAccessAPI(@RequestBody OPADataRequest opaDataRequest);
}

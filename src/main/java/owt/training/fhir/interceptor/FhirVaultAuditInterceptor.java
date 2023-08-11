package owt.training.fhir.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import owt.training.fhir.auth.dto.DomainResourceWrapper;
import owt.training.fhir.auth.dto.FHIRClaim;
import owt.training.fhir.auth.util.FhirContextUtil;
import owt.training.fhir.auth.util.JWTTokenUtil;

import java.util.Date;

public class FhirVaultAuditInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FhirVaultAuditInterceptor.class);

    private static final IParser iParser;

    static {
        iParser = FhirContextUtil.getiParser();
    }

    @Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
    public void successResponse(RequestDetails requestDetails, ResponseDetails responseDetails) {
        AuditEvent auditEvent = buildAuditSuccessEvent(requestDetails, responseDetails);
        log.info("Success Response: {}", iParser.encodeResourceToString(auditEvent));
    }

    @Hook(Pointcut.SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME)
    public void failureOutgoingOperationOutcome(RequestDetails requestDetails,
                                                IBaseOperationOutcome iBaseOperationOutcome) {

        AuditEvent auditEvent = buildAuditFailEvent(requestDetails, iBaseOperationOutcome);
        log.info("Fail Request: {}", iParser.encodeResourceToString(auditEvent));
    }

    private AuditEvent buildAuditFailEvent(RequestDetails requestDetails, IBaseOperationOutcome iBaseOperationOutcome) {
        AuditEvent auditFailEvent = buildAuditEvent(requestDetails);
        setFailOutcome(auditFailEvent);
        return auditFailEvent;
    }

    private AuditEvent buildAuditSuccessEvent(RequestDetails requestDetails, ResponseDetails responseDetails) {
        AuditEvent auditSuccessEvent = buildAuditEvent(requestDetails);
        setSuccessOutcome(auditSuccessEvent);
        return auditSuccessEvent;
    }

    private void setFailOutcome(AuditEvent auditFailEvent) {
        setOutcome(auditFailEvent, "4", "Failure");
    }

    private void setSuccessOutcome(AuditEvent auditSuccessEvent) {
        setOutcome(auditSuccessEvent, "0", "Success");
    }

    private void setOutcome(AuditEvent auditEvent, String code, String display) {
        auditEvent.getOutcome().getCode()
                .setSystem("http://terminology.hl7.org/CodeSystem/audit-event-outcome")
                .setCode(code)
                .setDisplay(display);
    }

    private AuditEvent buildAuditEvent(RequestDetails requestDetails) {
        AuditEvent auditEvent = new AuditEvent();
        setId(auditEvent, requestDetails);
        setCategory(auditEvent);
        setRestfulInteraction(auditEvent, requestDetails);
        setAction(auditEvent, requestDetails);
        auditEvent.setRecorded(new Date());
        setAgent(auditEvent, requestDetails);
        setSource(auditEvent, requestDetails);
        setEntity(auditEvent, requestDetails);
        return auditEvent;
    }

    private void setAction(AuditEvent auditEvent, RequestDetails requestDetails) {
        auditEvent.setAction(AuditEvent.AuditEventAction.R);
    }

    private void setAgent(AuditEvent auditEvent, RequestDetails requestDetails) {
        AuditEvent.AuditEventAgentComponent auditEventAgentComponent = auditEvent.addAgent();

        auditEventAgentComponent.getType().addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/extra-security-role-type")
                .setCode("humanuser")
                .setDisplay("human user");

        Identifier whoIdentifier = auditEventAgentComponent.getWho().getIdentifier();
        FHIRClaim jwtDto = JWTTokenUtil.parsingJwtToken(requestDetails);
        DomainResourceWrapper userLoggingWrapper = new DomainResourceWrapper(jwtDto.getResourceType())
                .addIdentifier(jwtDto.getAccountUrn())
                .addIdentifier(jwtDto.getGlnUrn())
                .addIdentifier(jwtDto.getMpildUrn())
                .addExtension("group", jwtDto.getGroups())
                .addExtension("scope", jwtDto.getScope());
        whoIdentifier.setValue(userLoggingWrapper.getIdentifier().get(0).getValue());
        whoIdentifier.setSystem(userLoggingWrapper.getIdentifier().get(0).getSystem());
    }

    private DomainResource buildUserLogin(FHIRClaim jwtDto) {
        DomainResourceWrapper userLoggingWrapper = new DomainResourceWrapper(jwtDto.getResourceType())
                .addIdentifier(jwtDto.getAccountUrn())
                .addIdentifier(jwtDto.getGlnUrn())
                .addIdentifier(jwtDto.getMpildUrn())
                .addExtension("group", jwtDto.getGroups())
                .addExtension("scope", jwtDto.getScope());
        return userLoggingWrapper.getResource();
    }

    private void setSource(AuditEvent auditEvent, RequestDetails requestDetails) {
        AuditEvent.AuditEventSourceComponent auditEventSourceComponent = auditEvent.getSource();

        auditEventSourceComponent.getObserver().getIdentifier().setValue(requestDetails.getFhirServerBase());

        auditEventSourceComponent
                .addType()
                .addCoding()
                .setCode("3")
                .setDisplay("Web Server")
                .setSystem("http://terminology.hl7.org/CodeSystem/security-source-type");
    }

    private void setEntity(AuditEvent auditEvent, RequestDetails requestDetails) {
        AuditEvent.AuditEventEntityComponent entity = auditEvent.addEntity();

        entity.getWhat().setReference(requestDetails.getRequestPath());

        entity.getRole()
                .addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/object-role")
                .setCode("1")
                .setDisplay("Patient");
    }

    private void setRestfulInteraction(AuditEvent auditEvent, RequestDetails requestDetails) {
        RestOperationTypeEnum operationTypeEnum = requestDetails.getRestOperationType();
        auditEvent
                .getCode()
                .addCoding()
                .setSystem(RestOperationTypeEnum.VALUESET_IDENTIFIER)
                .setCode(operationTypeEnum.getCode())
                .setDisplay(operationTypeEnum.getCode());
    }

    private void setId(AuditEvent auditEvent, RequestDetails requestDetails) {
        auditEvent.setId(requestDetails.getRequestId());
    }

    private void setCategory(AuditEvent auditEvent) {
        auditEvent.addCategory().addCoding()
                .setCode("rest")
                .setDisplay("Restful Operation")
                .setSystem("http://terminology.hl7.org/CodeSystem/audit-event-type");
    }
}

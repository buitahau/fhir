package owt.training.fhir.auth.dto.wrapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r5.model.*;
import owt.training.fhir.auth.exception.FhirVaultException;

import java.lang.reflect.Method;
import java.util.List;

public class DomainResourceWrapper {

    private DomainResource resource;

    private final String resourceType;

    public DomainResourceWrapper(String resourceType) {
        Validate.notNull(resourceType);
        this.resourceType = resourceType;
        generateResource();
    }

    private void generateResource() {
        if ("Patient".equals(resourceType)) {
            resource = new Patient();
        } else if ("Practitioner".equals(resourceType)) {
            resource = new Practitioner();
        } else if ("RelatedPerson".equals(resourceType)) {
            resource = new RelatedPerson();
        } else if ("Goal".equals(resourceType)) {
            resource = new Goal();
        } else if ("Observation".equals(resourceType)) {
            resource = new Observation();
        } else if ("Organization".equals(resourceType)) {
            resource = new Organization();
        } else {
            resource = new Person();
        }
    }

    public DomainResourceWrapper addIdentifier(String valueIdentifier) {
        if (StringUtils.isBlank(valueIdentifier)) {
            return this;
        }

        int lastIndexColon = valueIdentifier.lastIndexOf(":");

        getIdentifier().add(new Identifier()
                .setSystem(valueIdentifier.substring(0, lastIndexColon))
                .setValue(valueIdentifier.substring(lastIndexColon + 1)));

        return this;
    }

    public DomainResourceWrapper addExtension(String extensionCode, List<String> extensionValue) {
        Extension extension = new Extension();
        extension.setId(extensionCode);

        CodeableConcept codeableConcept = new CodeableConcept();
        extension.setValue(codeableConcept);

        for (String value : extensionValue) {
            codeableConcept.addCoding(new Coding().setCode(value));
        }

        getResource().addExtension(extension);

        return this;
    }

    public DomainResource getResource() {
        return resource;
    }

    public List<Identifier> getIdentifier() {
        return (List<Identifier>) invokeMethod("getIdentifier");
    }

    private Object invokeMethod(final String methodName) {
        try {
            Method method = this.resource.getClass().getMethod(methodName);
            return method.invoke(this.resource);
        } catch (Exception e) {
            throw new FhirVaultException("Error invoking method " + methodName, e);
        }
    }
}

package owt.training.fhir.interceptor.dto;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.lang.reflect.Method;
import java.util.List;

public class DomainResourceWrapper {

    private DomainResource resource;

    private final String resourceName;

    public DomainResourceWrapper(String resourceName) {
        this.resourceName = resourceName;
        generateResource();
    }

    private void generateResource() {
        if ("Patient".equals(resourceName)) {
            resource = new Patient();
        } else if ("Practitioner".equals(resourceName)) {
            resource = new Practitioner();
        } else if ("RelatedPerson".equals(resourceName)) {
            resource = new RelatedPerson();
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

    public DomainResource getResource() {
        return resource;
    }

    public List<Identifier> getIdentifier() {
        return (List<Identifier>) invokeMethod("getIdentifier");
    }

    private Object invokeMethod(String methodName) {
        try {
            Method method = this.resource.getClass().getMethod(methodName);
            return method.invoke(this.resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package owt.training.fhir.auth.dto;

import org.hl7.fhir.r5.model.Reference;

import java.util.Objects;

public class ReferenceWrapper {

    private Reference reference;

    public ReferenceWrapper(Reference reference) {
        this.reference = reference;
    }

    public Reference getReference() {
        return reference;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference.getReference());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferenceWrapper that = (ReferenceWrapper) o;
        return Objects.equals(reference.getReference(), that.reference.getReference());
    }
}

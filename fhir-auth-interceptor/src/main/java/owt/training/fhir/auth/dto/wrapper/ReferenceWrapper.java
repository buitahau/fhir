package owt.training.fhir.auth.dto.wrapper;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r5.model.Reference;

import java.util.Objects;

public class ReferenceWrapper {

    private Reference reference;

    public ReferenceWrapper(Reference reference) {
        Validate.notNull(reference);
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

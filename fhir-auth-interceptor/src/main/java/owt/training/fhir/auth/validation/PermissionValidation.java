package owt.training.fhir.auth.validation;

import org.hl7.fhir.r5.model.Period;
import org.hl7.fhir.r5.model.Permission;
import owt.training.fhir.auth.exception.FhirVaultException;

import java.util.Date;

public class PermissionValidation {

    private PermissionValidation() {
    }

    public static boolean isValid(Permission permission) {
        try {
            validateStatus(permission.getStatus());
            validatePeriod(permission.getValidity());
        } catch (FhirVaultException e) {
            return false;
        }
        return true;
    }

    private static void validatePeriod(Period period) {
        if (period == null) {
            return;
        }

        Date currentDate = new Date();
        if (period.getStart() != null && currentDate.before(period.getStart())) {
            throw new FhirVaultException("Start date not before current date.");
        }

        if (period.getEnd() != null && currentDate.after(period.getEnd())) {
            throw new FhirVaultException("End date not after current date.");
        }
    }

    private static void validateStatus(Permission.PermissionStatus status) {
        if (Permission.PermissionStatus.ACTIVE != status) {
            throw new FhirVaultException("Invalid status " + status + " permission");
        }
    }
}

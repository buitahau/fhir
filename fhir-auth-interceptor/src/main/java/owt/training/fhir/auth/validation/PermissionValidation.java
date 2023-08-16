package owt.training.fhir.auth.validation;

import org.hl7.fhir.r5.model.Permission;

public class PermissionValidation {

    private PermissionValidation() {}

    public static boolean isValid(Permission permission) {
        // TODO Validate status, validity field
        return true;
    }

    public static boolean isValid(Permission.RuleComponent ruleComponent) {
        // TODO Validate period field
        return true;
    }
}

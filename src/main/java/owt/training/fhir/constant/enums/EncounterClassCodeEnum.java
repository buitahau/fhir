package owt.training.fhir.constant.enums;

import org.apache.commons.lang3.StringUtils;

public enum EncounterClassCodeEnum {
    IMP, AMB, ORSENC, EMER, VR, HH;

    public static EncounterClassCodeEnum fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (EncounterClassCodeEnum classCode : EncounterClassCodeEnum.values()) {
            if (StringUtils.equals(classCode.name().toLowerCase(), code.toLowerCase())) {
                return classCode;
            }
        }
        throw new RuntimeException("Invalid Encounter class code.");
    }
}

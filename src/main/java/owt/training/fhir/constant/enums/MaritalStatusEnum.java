package owt.training.fhir.constant.enums;

import org.apache.commons.lang3.StringUtils;

public enum MaritalStatusEnum {
    ANNULLED("A"), DIVORCED("D"), INTERLOCUTORY("I"), MARRIED("M");

    private String code;

    MaritalStatusEnum(String code) {
        this.code = code;
    }

    public static MaritalStatusEnum fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (MaritalStatusEnum maritalStatusEnum : MaritalStatusEnum.values()) {
            if (StringUtils.equals(code.toLowerCase(), maritalStatusEnum.getCode().toLowerCase())) {
                return maritalStatusEnum;
            }
        }
        throw new RuntimeException("Invalid marital status code");
    }

    public String getCode() {
        return code;
    }
}

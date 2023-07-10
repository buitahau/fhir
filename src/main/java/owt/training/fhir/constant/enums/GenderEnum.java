package owt.training.fhir.constant.enums;

import org.apache.commons.lang3.StringUtils;

public enum GenderEnum {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other"),
    UNKNOWN("Unknown");

    private String code;

    GenderEnum(String code) {
        this.code = code;
    }

    public static GenderEnum fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (GenderEnum gender : GenderEnum.values()) {
            if (StringUtils.equals(code.toLowerCase(), gender.getCode().toLowerCase())) {
                return gender;
            }
        }
        throw new RuntimeException("Invalid gender code");
    }

    public String getCode() {
        return code;
    }
}

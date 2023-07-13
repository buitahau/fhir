package owt.training.fhir.constant.enums;

import org.apache.commons.lang3.StringUtils;

public enum EncounterStatusEnum {
    PLANNED("planned"),
    IN_PROGRESS("in-progress"),
    ON_HOLD("on-hold"),
    DISCHARGED("discharged"),
    FINISHED("finished"),
    CANCELLED("cancelled"),
    DISCONTINUED("discontinued"),
    ENTERED_IN_ERROR("entered-n-error"),
    UNKNOWN("unknown");

    private String code;

    EncounterStatusEnum(String code) {
        this.code = code;
    }

    public static EncounterStatusEnum fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (EncounterStatusEnum status : EncounterStatusEnum.values()) {
            if (StringUtils.equals(status.code.toLowerCase(), code.toLowerCase())) {
                return status;
            }
        }
        throw new RuntimeException("Invalid Encounter status code");
    }

    public String getCode() {
        return code;
    }
}

package owt.training.fhir.constant.enums;

import org.apache.commons.lang3.StringUtils;

public enum EpisodeOfCareStatusEnum {
    PLANNED("planned"),
    WAIT_LIST("waitlist"),
    ACTIVE("active"),
    ON_HOLD("onhold"),
    FINISHED("finished"),
    CANCELLED("cancelled"),
    ENTERED_IN_ERROR("entered-in-error");

    private String code;

    EpisodeOfCareStatusEnum(String code) {
        this.code = code;
    }

    public static EpisodeOfCareStatusEnum fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (EpisodeOfCareStatusEnum statusEnum : EpisodeOfCareStatusEnum.values()) {
            if (StringUtils.equals(statusEnum.code.toLowerCase(), code.toLowerCase())) {
                return statusEnum;
            }
        }
        throw new RuntimeException("Wrong Episode Of Care code");
    }

    public String getCode() {
        return code;
    }
}

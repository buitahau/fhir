package com.owt.trackingworkingtime.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TrackingRequestDto {
    @NotNull(message = "List tag id can not be null.")
    private List<String> tagIds;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date fromDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date toDate;
}

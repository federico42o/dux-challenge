package org.f420.duxchallenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@Builder
public class ErrorResponse {
    private String errorMessage;
    private Integer statusCode;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime timestamp;
}

package org.f420.duxchallenge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@Builder
public class ErrorResponse {
    private String errorMessage;
    private Integer statusCode;
    private ZonedDateTime timestamp;
}

package org.f420.duxchallenge.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.f420.duxchallenge.enums.ErrorMessage;

import java.io.Serializable;
import java.text.MessageFormat;

@Getter
@Setter
@ToString
public class ApiException extends RuntimeException {
    private final Integer statusCode;
    private final Serializable[] data;

    public ApiException(String message, Integer statusCode, Serializable... data) {
        super(message);
        this.statusCode = statusCode;
        this.data = data;
    }

    public ApiException(ErrorMessage errorMessage, Serializable... data) {
        super(errorMessage.getMessage());
        this.statusCode = errorMessage.getStatusCode();
        this.data = data;
    }

    public String getErrorMessage() {
        return this.getMessage() != null ? MessageFormat.format(this.getMessage(), (Object[])this.data) : null;
    }

}

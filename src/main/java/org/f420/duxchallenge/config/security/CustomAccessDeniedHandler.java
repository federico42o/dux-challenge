package org.f420.duxchallenge.config.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.f420.duxchallenge.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;

import static org.f420.duxchallenge.constants.Constants.JSON_CONTENT_TYPE;
import static org.f420.duxchallenge.enums.ErrorMessage.ACCESS_DENIED;
import static org.f420.duxchallenge.utils.ConvertUtils.toObjectString;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(JSON_CONTENT_TYPE);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(ACCESS_DENIED.getMessage())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .timestamp(ZonedDateTime.now())
                .build();
        response.getWriter().write(toObjectString(errorResponse));
    }
}

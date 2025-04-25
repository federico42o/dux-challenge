package org.f420.duxchallenge.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    UNEXPECTED_ERROR("Error inesperado", 500),
    JSON_PROCESSING_ERROR("Error al procesar el JSON", 400),
    ENTITY_NOT_FOUND("La entidad con identificador {0} no se ha encontrado", 404),
    BAD_REQUEST("La solicitud es inválida", 400),
    TEAM_ALREADY_EXISTS("El equipo {0} ya está registado {1}", 400),
    TEAM_REGISTERED_IN_ANOTHER_LEAGUE("El equipo {0} ya está registado en otra liga: {1}", 400),
    BAD_CREDENTIALS("Usuario o contraseña incorrectos", 401),
    USERNAME_NOT_FOUND("El usuario {0} no fue encontrado.", 404),
    MALFORMED_JWT("Token inválido", 401),
    ACCESS_DENIED("No tiene permisos para acceder a este recurso", 403),
    ID_REQUIRED("El campo ID es requerido", 400),
    ENTITY_ALREADY_EXISTS("Ya existe un registro con estas propiedades", 400),
    ;

    private final String message;
    private final Integer statusCode;

    ErrorMessage(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}

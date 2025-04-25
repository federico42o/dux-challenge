package org.f420.duxchallenge.resource.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractResource {

    protected  <T> ResponseEntity<T> response(T data) {
        return ResponseEntity.ok(data);
    }

    protected ResponseEntity<Void> emptyResponse() {
        return ResponseEntity.noContent().build();
    }

    protected  <T> ResponseEntity<T> responseCreated(T data) {
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }
}

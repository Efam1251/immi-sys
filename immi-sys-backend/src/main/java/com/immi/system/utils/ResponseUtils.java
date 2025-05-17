package com.immi.system.utils;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
    // Standard success response
    public static ResponseEntity<?> success(Object data) {
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    // Standard error response
    public static ResponseEntity<?> error(String message, HttpStatus status) {
        return new ResponseEntity<>(Map.of("error", message), status);
    }

    // Not found response
    public static ResponseEntity<?> notFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

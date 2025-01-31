package org.gescomlbank.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    public static ResponseEntity<Map<String, Object>> successResponse(String message, Object data) {
        return buildResponse(message, data, HttpStatus.OK, true);
    }

    public static ResponseEntity<Map<String, Object>> errorsResponse(String message, HttpStatus status) {
        return buildResponse(message, null, status, false);
    }

    private static ResponseEntity<Map<String, Object>> buildResponse(String message, Object data, HttpStatus status, boolean success) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);

        return ResponseEntity.status(status).body(response);
    }
}

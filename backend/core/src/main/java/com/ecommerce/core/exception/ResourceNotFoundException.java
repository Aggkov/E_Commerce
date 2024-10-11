package com.ecommerce.core.exception;

import java.io.Serial;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1882526982400865317L;

    private String message;
    private HttpStatus status;
    private Integer statusCode;
    private Instant timestamp;

    public ResourceNotFoundException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}

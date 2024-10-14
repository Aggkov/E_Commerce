package com.ecommerce.core.exception;

import java.io.Serial;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1095661800213238593L;

    private String message;
    private HttpStatus status;
    private Integer statusCode;
    private Instant timestamp;

    public BadRequestException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}

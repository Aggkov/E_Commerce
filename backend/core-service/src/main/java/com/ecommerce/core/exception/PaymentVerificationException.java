package com.ecommerce.core.exception;

import java.io.Serial;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVerificationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5523699532269890472L;
    private String message;
    private String timeStamp;

    public PaymentVerificationException(String message) {
        this.message = message;
    }
}

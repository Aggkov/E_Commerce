package com.me.ecommerce.dto.request;

import com.me.ecommerce.dto.response.StateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingAddressDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3995643498964980631L;

    private UUID id;

    @NotBlank(message = "city is required")
    @Size(min = 2, message = "city must be at least 2 characters long")
    private String city;

    @NotBlank(message = "street is required")
    @Size(min = 2, message = "street must be at least 2 characters long")
    private String street;

    @NotBlank(message = "zipCode is required")
    @Pattern(regexp = "\\d{5}", message = "Zip Code must be exactly 5 digits")
    private String zipCode;

    private StateDTO state;
}

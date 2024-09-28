package com.me.ecommerce.dto.response;

import com.me.ecommerce.dto.request.BillingAddressDTO;
import com.me.ecommerce.dto.request.ShippingAddressDTO;
import jakarta.validation.constraints.Email;
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
public class CustomerDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -9167621179769238334L;

//    private UUID id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, message = "First name must be at least 2 characters long")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, message = "Last name must be at least 2 characters long")
    private String lastName;

    @Email(message = "Invalid email format") // Basic email format check
    @NotBlank(message = "email is required")
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Email should match the required pattern")
    private String email;

    private ShippingAddressDTO shippingAddress;

    private BillingAddressDTO billingAddress;
}

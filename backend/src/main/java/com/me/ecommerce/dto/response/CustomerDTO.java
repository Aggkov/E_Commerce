package com.me.ecommerce.dto.response;

import com.me.ecommerce.dto.request.BillingAddressDTO;
import com.me.ecommerce.dto.request.ShippingAddressDTO;
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

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private ShippingAddressDTO shippingAddress;

    private BillingAddressDTO billingAddress;
}

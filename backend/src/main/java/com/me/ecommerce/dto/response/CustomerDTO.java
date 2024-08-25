package com.me.ecommerce.dto.response;

import com.me.ecommerce.dto.request.BillingAddressDTO;
import com.me.ecommerce.dto.request.ShippingAddressDTO;
import com.me.ecommerce.entity.Address;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -9167621179769238334L;

    private Integer customerId;

    private String firstName;

    private String lastName;

    private String email;

    private BillingAddressDTO billingAddressDTO;

    private ShippingAddressDTO shippingAddressDTO;
}

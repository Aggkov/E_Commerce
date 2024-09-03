package com.me.ecommerce.dto.request;

import com.me.ecommerce.dto.response.StateDTO;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingAddressDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7136544323694862444L;

    private UUID id;

    private String city;

    private String street;

    private String zipCode;

    private StateDTO state;
}

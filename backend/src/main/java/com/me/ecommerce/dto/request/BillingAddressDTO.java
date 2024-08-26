package com.me.ecommerce.dto.request;

import com.me.ecommerce.dto.response.StateDTO;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingAddressDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7136544323694862444L;

    private Integer id;

    private String city;

    private String street;

    private String zipCode;

    private StateDTO stateDTO;
}

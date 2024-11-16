package com.ecommerce.notification.kafka;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingAddressDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2877577101632998734L;
    private UUID id;
    private String city;
    private String street;
    private String zipCode;
    private StateDTO state;

    @Override
    public String toString() {
        return "ShippingAddressDTO{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", state=" + state +
                '}';
    }
}

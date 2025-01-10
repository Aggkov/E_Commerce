package com.ecommerce.core.kafka.event.order;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Builder
public class ShippingAddress implements Serializable {
    @Serial
    private static final long serialVersionUID = -2638152026179414300L;
//    private UUID id;
    private String city;
    private String street;
    private String zipCode;
    private State state;

    @Override
    public String toString() {
        return "ShippingAddressDTO{" +
//                "id=" + id +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", state=" + state +
                '}';
    }
}

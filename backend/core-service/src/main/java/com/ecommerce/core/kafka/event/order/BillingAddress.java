package com.ecommerce.core.kafka.event.order;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Builder
public class BillingAddress implements Serializable {
    @Serial
    private static final long serialVersionUID = -2036487321606591402L;
//    private UUID id;
    private String city;
    private String street;
    private String zipCode;
    private State state;

    @Override
    public String toString() {
        return "BillingAddressDTO{" +
//                "id=" + id +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", state=" + state +
                '}';
    }
}

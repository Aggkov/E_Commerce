package com.ecommerce.notification.kafka.event.order;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -4921358887322532581L;
    private String firstName;
    private String lastName;
    private String email;
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;

    @Override
    public String toString() {
        return "UserDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", shippingAddress=" + shippingAddress +
                ", billingAddress=" + billingAddress +
                '}';
    }
}

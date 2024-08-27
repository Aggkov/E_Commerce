package com.me.ecommerce.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "zip_code")
    private String zipCode;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_shipping_address_id", nullable = false)
    private Customer shippingAddressCustomer;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_billing_address_id", nullable = false)
    private Customer billingAddressCustomer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id)
                && Objects.equals(city, address.city)
                && Objects.equals(street, address.street)
                && Objects.equals(zipCode, address.zipCode)
                && Objects.equals(billingAddressCustomer, address.billingAddressCustomer)
                && Objects.equals(shippingAddressCustomer, address.shippingAddressCustomer)
                && Objects.equals(state, address.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, street, zipCode, billingAddressCustomer, shippingAddressCustomer, state);
    }
}
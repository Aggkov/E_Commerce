package com.me.ecommerce.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "shippingAddressCustomer", cascade = CascadeType.PERSIST)
    private Address shippingAddress;
    /*
    When you save or delete a Customer,
    you might want the associated Address
    to be saved or deleted automatically.
     */
    @OneToOne(mappedBy = "billingAddressCustomer", cascade = CascadeType.PERSIST)
    private Address billingAddress;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Order> orders = new LinkedHashSet<>();

    public void add(Order order) {
        if (order != null) {
            if (orders == null) {
                orders = new LinkedHashSet<>();
            }
            orders.add(order);
            order.setCustomer(this);
        }
    }

    public void setAddress(Address address) {
        this.setShippingAddress(address);
        address.setShippingAddressCustomer(this);
        this.setBillingAddress(address);
        address.setBillingAddressCustomer(this);
    }
}
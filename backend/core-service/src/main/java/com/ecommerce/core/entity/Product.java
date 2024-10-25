package com.ecommerce.core.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends Audit {
    @Serial
    private static final long serialVersionUID = -6188767059145560467L;

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "sku")
    private String sku;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "unit_price", precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "image_url")
    private String imageUrl;

    @ColumnDefault("true")
    @Column(name = "active")
    private boolean active;

    @Column(name = "units_in_stock")
    private int unitsInStock;

    @Column(name = "units_sold")
    private Integer unitsSold;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    public void addOrderItem(OrderItem orderItem) {
        if (orderItem != null) {
            if (orderItems == null) {
                orderItems = new LinkedHashSet<>();
            }
            orderItems.add(orderItem);
            orderItem.setProduct(this);
        }
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//        Product product = (Product) o;
//        return active == product.active &&
//                unitsInStock == product.unitsInStock &&
//                Objects.equals(id, product.id) &&
//                Objects.equals(sku, product.sku) &&
//                Objects.equals(name, product.name) &&
//                Objects.equals(description, product.description) &&
//                Objects.equals(unitPrice, product.unitPrice) &&
//                Objects.equals(imageUrl, product.imageUrl) &&
//                Objects.equals(unitsSold, product.unitsSold) &&
//                Objects.equals(category, product.category);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, sku,
                name, description, unitPrice, imageUrl,
                active, unitsInStock, unitsSold, category);
    }
}
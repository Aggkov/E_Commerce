package com.me.ecommerce.entity;

//import jakarta.persistence;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends Audit {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_gen")
//    @SequenceGenerator(name = "product_id_gen", sequenceName = "product_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;

    @ColumnDefault("NULL::character varying")
    @Column(name = "sku")
    private String sku;

    @ColumnDefault("NULL::character varying")
    @Column(name = "name")
    private String name;

    @ColumnDefault("NULL::character varying")
    @Column(name = "description")
    private String description;

    @ColumnDefault("NULL::numeric")
    @Column(name = "unit_price", precision = 13, scale = 2)
    private BigDecimal unitPrice;

    @ColumnDefault("NULL::character varying")
    @Column(name = "image_url")
    private String imageUrl;

    @ColumnDefault("true")
    @Column(name = "active")
    private boolean active;

    @Column(name = "units_in_stock")
    private int unitsInStock;

    @Column(name = "units_sold")
    private Integer unitsSold;

//    @Column(name = "date_created")
//    private Instant dateCreated;
//
//    @Column(name = "last_updated")
//    private Instant lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
//    @JsonBackReference
    private ProductCategory category;

}
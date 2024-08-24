package com.me.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "country")
public class Country {
    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "code", length = 2)
    private String code;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "country")
    private Set<State> states = new LinkedHashSet<>();
}
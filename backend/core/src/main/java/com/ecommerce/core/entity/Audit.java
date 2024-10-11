package com.ecommerce.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = { "createdAt", "updatedAt" },
        allowGetters = true
)
public abstract class Audit implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedDate
    @Column(
            nullable = false,
           updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(
            nullable = false
    )
    private Instant updatedAt;

//    @CreatedBy
//    @Column(updatable = false)
//    private Long createdBy;
//
//    @LastModifiedBy
//    private Long updatedBy;

}

package com.devsuperior.dscatalog.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "tb_category")
@NoArgsConstructor
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updateAt;


    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    @PrePersist
    public void prePresist() {
        createdAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateAt = Instant.now();
    }

//    public Category(Long id, String name) {
//        this.id = id;
//        this.name = name;
//    }
}

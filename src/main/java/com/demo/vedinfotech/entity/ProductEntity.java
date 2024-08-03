package com.demo.vedinfotech.entity;

import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductEntity {

    @Id
    @GeneratedValue(generator = "custom-id-generator")
    @GenericGenerator(
            name = "custom-id-generator",
            strategy = "com.demo.vedinfotech.generator.CustomProductIdGenerator"
    )
    private Long id;

    private String name;
    private String description;
    private Double price;
    private String category;
}

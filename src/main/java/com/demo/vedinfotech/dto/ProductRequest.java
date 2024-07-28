package com.demo.vedinfotech.dto;

import com.demo.vedinfotech.entity.Activity;
import com.demo.vedinfotech.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private ProductEntity product;
    private Activity activity;
}

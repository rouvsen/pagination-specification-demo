package com.rouvsen.examples.paginationspecificationdemo.model.dto;

import com.rouvsen.examples.paginationspecificationdemo.dao.entity.ProductEntity;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private LocalDateTime createdAt;

    public static ProductDTO fromEntity(ProductEntity product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .build();
    }
}


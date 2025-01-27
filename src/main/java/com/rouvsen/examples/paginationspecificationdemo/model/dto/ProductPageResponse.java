package com.rouvsen.examples.paginationspecificationdemo.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPageResponse {
    private List<ProductDTO> products;
    private String nextCursor;
    private boolean hasNext;
}


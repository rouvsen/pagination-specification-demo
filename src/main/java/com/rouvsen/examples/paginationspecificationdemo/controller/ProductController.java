package com.rouvsen.examples.paginationspecificationdemo.controller;

import com.rouvsen.examples.paginationspecificationdemo.model.dto.ProductPageResponse;
import com.rouvsen.examples.paginationspecificationdemo.service.ProductService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Fetch products with cursor-based pagination.
     *
     * @param name     Optional name filter (partial match)
     * @param category Optional category filter (exact match)
     * @param minPrice Optional minimum price filter
     * @param maxPrice Optional maximum price filter
     * @param size     Number of records per page (max 100)
     * @param cursor   Optional cursor (createdAt timestamp)
     * @param sortDir  Sort direction ("asc" or "desc")
     * @return ProductPageResponse
     */
    @GetMapping
    public ProductPageResponse getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "20") @Min(1) int size,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return productService.getProducts(name, category, minPrice, maxPrice, size, cursor, sortDir);
    }
}


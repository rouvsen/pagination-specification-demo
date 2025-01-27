package com.rouvsen.examples.paginationspecificationdemo.service;

import com.rouvsen.examples.paginationspecificationdemo.dao.entity.ProductEntity;
import com.rouvsen.examples.paginationspecificationdemo.dao.repository.ProductRepository;
import com.rouvsen.examples.paginationspecificationdemo.model.dto.ProductDTO;
import com.rouvsen.examples.paginationspecificationdemo.model.dto.ProductPageResponse;
import com.rouvsen.examples.paginationspecificationdemo.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Fetch products using cursor-based pagination.
     *
     * @param name     Filter by name (partial match)
     * @param category Filter by category (exact match)
     * @param minPrice Minimum price filter
     * @param maxPrice Maximum price filter
     * @param size     Number of records per page
     * @param cursor   The cursor timestamp (createdAt)
     * @param sortDir  Sort direction ("asc" or "desc")
     * @return ProductPageResponse containing products and next cursor
     */
    public ProductPageResponse getProducts(String name, String category, Double minPrice, Double maxPrice,
                                           int size, String cursor, String sortDir) {
        // Validate size
        if (size > 100) {
            size = 100; // Limit maximum size to prevent performance issues
        }

        // Build Specifications
        Specification<ProductEntity> spec = Specification
                .where(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasCategory(category))
                .and(ProductSpecification.priceGreaterThanOrEqual(minPrice))
                .and(ProductSpecification.priceLessThanOrEqual(maxPrice));

        // Determine sort direction
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Sort by createdAt and id to ensure unique ordering
        Sort sort = Sort.by(direction, "createdAt").and(Sort.by(direction, "id"));

        // Create Pageable with size + 1 to check if there's a next page
        Pageable pageable = PageRequest.of(0, size + 1, sort);

        List<ProductEntity> products;

        if (cursor != null && !cursor.isBlank()) {
            // Parse cursor
            LocalDateTime cursorTime = LocalDateTime.parse(cursor);

            if (direction == Sort.Direction.ASC) {
                // Fetch products created after the cursor
                products = productRepository.findByCreatedAtAfterOrderByCreatedAtAsc(cursorTime, pageable);
            } else {
                // Fetch products created before the cursor
                products = productRepository.findByCreatedAtBeforeOrderByCreatedAtDesc(cursorTime, pageable);
            }
        } else {
            // No cursor provided, fetch the first page
            products = productRepository.findAll(spec, pageable).stream().toList();
        }

        // Determine if there's a next page
        boolean hasNext = products.size() > size;

        // Trim the extra record if exists
        if (hasNext) {
            products = products.subList(0, size);
        }

        // Prepare next cursor
        String nextCursor = null;
        if (hasNext) {
            ProductEntity lastProduct = products.get(products.size() - 1);
            nextCursor = lastProduct.getCreatedAt().toString();
        }

        // Convert to DTOs
        List<ProductDTO> productDTOs = products.stream()
                .map(ProductDTO::fromEntity)
                .toList();

        return ProductPageResponse.builder()
                .products(productDTOs)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}

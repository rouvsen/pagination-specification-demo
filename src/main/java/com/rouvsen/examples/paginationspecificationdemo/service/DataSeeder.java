package com.rouvsen.examples.paginationspecificationdemo.service;

import com.rouvsen.examples.paginationspecificationdemo.dao.entity.ProductEntity;
import com.rouvsen.examples.paginationspecificationdemo.dao.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final ProductRepository productRepository;

    @PostConstruct
    public void seedData() {
        if (productRepository.count() == 0) {
            IntStream.rangeClosed(1, 300).forEach(i -> {
                ProductEntity product = ProductEntity.builder()
                        .name("Product " + i)
                        .category(i % 2 == 0 ? "Electronics" : "Books")
                        .price(10.0 + i)
                        .build();
                productRepository.save(product);
            });
            System.out.println("Seeded 300 products.");
        }
    }

    /*
    1. Fetch First Page
    GET http://localhost:8080/api/products?size=25&sortDir=asc
    2. Fetch Next Page Using Cursor. Use the nextCursor from the previous response.
    GET http://localhost:8080/api/products?size=25&cursor=2025-01-27T10:00:00&sortDir=asc
    3. Handling No More Pages. When you reach the end, hasNext will be false and nextCursor will be null.
    GET http://localhost:8080/api/products?size=100&cursor=2025-01-27T10:05:00&sortDir=asc
    4. Error Scenarios
    Invalid Cursor Format: GET http://localhost:8080/api/products?size=25&cursor=invalid-timestamp&sortDir=asc
    Response:
    {
      "status": 400,
      "error": "Invalid value for parameter 'cursor'. Expected type: LocalDateTime"
    }
    5. Exceeding Maximum Page Size:
    GET http://localhost:8080/api/products?size=1000&sortDir=asc
    {
        "products": [100 products],
        "nextCursor": "2025-01-27T10:10:00",
        "hasNext": true
    }
    Explanation: Size is capped at 100.
    * */
}


package com.rouvsen.examples.paginationspecificationdemo.dao.repository;

import com.rouvsen.examples.paginationspecificationdemo.dao.entity.ProductEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    // Fetch products created after a certain timestamp (cursor)
    List<ProductEntity> findByCreatedAtAfterOrderByCreatedAtAsc(LocalDateTime createdAt, Pageable pageable);

    // Alternatively, for descending order
    List<ProductEntity> findByCreatedAtBeforeOrderByCreatedAtDesc(LocalDateTime createdAt, Pageable pageable);
}

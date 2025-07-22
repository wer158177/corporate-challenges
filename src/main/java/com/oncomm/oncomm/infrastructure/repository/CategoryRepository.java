package com.oncomm.oncomm.infrastructure.repository;

import com.oncomm.oncomm.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
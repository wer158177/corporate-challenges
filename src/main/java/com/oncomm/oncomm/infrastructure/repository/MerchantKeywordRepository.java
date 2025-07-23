package com.oncomm.oncomm.infrastructure.repository;

import com.oncomm.oncomm.domain.model.Category;
import com.oncomm.oncomm.domain.model.ClassificationKeyword;
import com.oncomm.oncomm.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MerchantKeywordRepository extends JpaRepository<ClassificationKeyword, Long> {
    Optional<ClassificationKeyword> findByCompanyAndCategoryAndKeyword(Company company, Category category, String keyword);

    @Query("SELECT mk FROM ClassificationKeyword mk " +
            "JOIN FETCH mk.company " +
            "JOIN FETCH mk.category")
    List<ClassificationKeyword> findAllWithJoins();


    List<ClassificationKeyword> findAllByCompanyAndCategory(Company company, Category category);

}

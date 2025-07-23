package com.oncomm.oncomm.infrastructure.repository;

import com.oncomm.oncomm.domain.model.Category;
import com.oncomm.oncomm.domain.model.Company;
import com.oncomm.oncomm.domain.model.MerchantKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MerchantKeywordRepository extends JpaRepository<MerchantKeyword, Long> {
    Optional<MerchantKeyword> findByCompanyAndCategoryAndKeyword(Company company, Category category, String keyword);

    @Query("SELECT mk FROM MerchantKeyword mk " +
            "JOIN FETCH mk.company " +
            "JOIN FETCH mk.category")
    List<MerchantKeyword> findAllWithJoins();

}

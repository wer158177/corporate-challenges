package com.oncomm.oncomm.infrastructure.repository;

import com.oncomm.oncomm.domain.model.MerchantKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MerchantKeywordRepository extends JpaRepository<MerchantKeyword, Long> {
    List<MerchantKeyword> findAllByCompany_CompanyId(String companyId);
}

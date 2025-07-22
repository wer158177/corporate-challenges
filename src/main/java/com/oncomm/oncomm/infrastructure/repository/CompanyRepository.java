package com.oncomm.oncomm.infrastructure.repository;

import com.oncomm.oncomm.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, String> {
}
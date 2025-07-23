package com.oncomm.oncomm.infrastructure.repository;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassifiedTransactionRepository extends JpaRepository<ClassifiedTransaction, Long> {

//    @Query("SELECT ct FROM ClassifiedTransaction ct " +
//            "JOIN FETCH ct.company " +
//            "JOIN FETCH ct.category " +
//            "WHERE ct.company.companyId = :companyId " +
//            "ORDER BY ct.classifiedAt DESC")
//    List<ClassifiedTransaction> findAllWithJoinsByCompanyId(String companyId);


    @Query("SELECT ct FROM ClassifiedTransaction ct " +
            "JOIN FETCH ct.company " +
            "JOIN FETCH ct.category " +
            "JOIN FETCH ct.transaction " +  // ✅ 수정
            "WHERE ct.company.companyId = :companyId " +
            "ORDER BY ct.classifiedAt DESC")
    List<ClassifiedTransaction> findAllWithJoinsByCompanyId(@Param("companyId") String companyId);



}
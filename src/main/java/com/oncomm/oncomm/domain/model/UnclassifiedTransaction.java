package com.oncomm.oncomm.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UnclassifiedTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tx_id")
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;  // 회사 정보 추가

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private boolean reviewed;

    public static UnclassifiedTransaction from(Transaction tx, Company company) {
        return UnclassifiedTransaction.builder()
                .transaction(tx)
                .company(company)
                .reason("키워드 미일치")
                .reviewed(false)
                .build();
    }
}
package com.oncomm.oncomm.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ClassifiedTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classified_tx_id")
    private Long classifiedTxId; // id → classifiedTxId로 변경

    @OneToOne(fetch = FetchType.LAZY)  // ManyToOne → OneToOne으로 변경
    @JoinColumn(name = "tx_id")
    private Transaction transaction;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(length = 100, nullable = false)
    private String companyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 100, nullable = false)
    private String categoryName;

    @Column(length = 200, nullable = false)
    private String matchedKeyword;

    private LocalDateTime classifiedAt;

    public static ClassifiedTransaction from(Transaction tx, ClassificationKeyword mk) {
        return ClassifiedTransaction.builder()
                .transaction(tx)
                .company(mk.getCompany())
                .companyName(mk.getCompany().getCompanyName())
                .category(mk.getCategory())
                .categoryName(mk.getCategory().getCategoryName())
                .matchedKeyword(mk.getKeyword())
                .classifiedAt(LocalDateTime.now())
                .build();
    }


    public LocalDateTime getOccurredAt() {
        return this.transaction.getOccurredAt();
    }

    public String getDescription() {
        return this.transaction.getDescription();
    }

    public long getDeposit() {
        return this.transaction.getDeposit();
    }

    public long getWithdraw() {
        return this.transaction.getWithdraw();
    }


}


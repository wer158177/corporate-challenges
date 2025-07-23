package com.oncomm.oncomm.domain.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * 미분류 거래 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UnclassifiedTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tx_id")
    private Transaction transaction; // 거래 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company; // 회사 정보

    @Column(nullable = false)
    private String reason; // 분류 실패 사유

    @Column(nullable = false)
    private boolean reviewed; // 검토 여부

    public static UnclassifiedTransaction from(Transaction transaction, Company company) {
        return UnclassifiedTransaction.builder()
                .transaction(transaction)
                .company(company)
                .reason("키워드 미일치")
                .reviewed(false)
                .build();
    }

    public static UnclassifiedTransaction from(Transaction tx, Company company, String reason) {
        return UnclassifiedTransaction.builder()
                .transaction(tx)
                .company(company)
                .reason(reason)
                .reviewed(false)
                .build();
    }


    public java.time.LocalDateTime getOccurredAt() {
        return transaction.getOccurredAt();
    }

    public String getDescription() {
        return transaction.getDescription();
    }

    public long getDeposit() {
        return transaction.getDeposit();
    }

    public long getWithdraw() {
        return transaction.getWithdraw();
    }

}
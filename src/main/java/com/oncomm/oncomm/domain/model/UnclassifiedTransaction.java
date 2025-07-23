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

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private boolean reviewed;

    // ✅ 여기에 정적 팩토리 메서드 추가!
    public static UnclassifiedTransaction from(Transaction tx) {
        return UnclassifiedTransaction.builder()
                .transaction(tx)
                .reason("키워드 미일치")
                .reviewed(false)
                .build();
    }
}

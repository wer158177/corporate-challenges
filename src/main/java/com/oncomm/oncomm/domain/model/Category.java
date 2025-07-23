package com.oncomm.oncomm.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 카테고리 엔티티: 회사별 분류 항목 정의
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Category {

    @Id
    @Column(length = 50)
    private String categoryId; // 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company; // 소속 회사

    @Column(length = 100, nullable = false)
    private String categoryName; // 카테고리 이름

    private LocalDateTime createdAt; // 생성 시각
}

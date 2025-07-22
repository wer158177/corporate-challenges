package com.oncomm.oncomm.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txId;

    private LocalDateTime txDatetime;
    private String description;
    private Long deposit;
    private Long withdraw;
    private Long balance;
    private String branchInfo;
    private LocalDateTime createdAt;
}
package com.oncomm.oncomm.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UnclassifiedTransaction {

    @Id
    private Long txId;

    @Column(columnDefinition = "TEXT")
    private String reason;

    private Boolean reviewed;
    private LocalDateTime reviewedAt;
}
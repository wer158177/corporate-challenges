package com.oncomm.oncomm.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Company {

    @Id
    @Column(length = 50)
    private String companyId;

    @Column(length = 100, nullable = false)
    private String companyName;

    private LocalDateTime createdAt;


}
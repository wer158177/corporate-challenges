package com.oncomm.oncomm.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class AccountingProcessRequest {

    private MultipartFile transactions;
    private MultipartFile rules;

    public AccountingProcessRequest(MultipartFile transactions, MultipartFile rules) {
        this.transactions = transactions;
        this.rules = rules;
    }
}
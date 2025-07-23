package com.oncomm.oncomm.controller;

import com.oncomm.oncomm.application.AccountingApplicationService;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounting")
@RequiredArgsConstructor
public class AccountingController {

    private final AccountingApplicationService accountingUseCase;

    @PostMapping("/process")
    public ResponseEntity<String> processAccounting(
            @RequestParam("transactions") MultipartFile transactions,
            @RequestParam("rules") MultipartFile rules
    ) {
        accountingUseCase.process(transactions, rules);
        return ResponseEntity.ok("처리 완료");
    }

    @GetMapping("/records")
    public ResponseEntity<List<ClassifiedTransactionResponse>> getRecords(
            @RequestParam("companyId") String companyId
    ) {
        return ResponseEntity.ok(accountingUseCase.getClassifiedTransactions(companyId));
    }
}

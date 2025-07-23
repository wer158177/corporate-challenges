package com.oncomm.oncomm.controller;

import com.oncomm.oncomm.application.AccountingApplicationService;
import com.oncomm.oncomm.application.TransactionQueryApplicationService;
import com.oncomm.oncomm.dto.response.AccountingSummary;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import com.oncomm.oncomm.dto.response.UnclassifiedTransactionResponse;
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
    private final TransactionQueryApplicationService queryService;


    @PostMapping("/process")
    public ResponseEntity<String> processAccounting(
            @RequestParam("transactions") MultipartFile transactions,
            @RequestParam("rules") MultipartFile rules
    ) {
        accountingUseCase.processTransactions(transactions, rules);
        return ResponseEntity.ok("처리 완료");
    }

    @GetMapping("/records")
    public ResponseEntity<List<ClassifiedTransactionResponse>> getClassifiedTransactions(
            @RequestParam String companyId) {
        List<ClassifiedTransactionResponse> transactions =
                queryService.getClassifiedTransactions(companyId);
        return ResponseEntity.ok(transactions);
    }


    @GetMapping("/summary/categories/{companyId}")
    public ResponseEntity<List<AccountingSummary>> getCategorySummaries(
            @PathVariable String companyId) {
        return ResponseEntity.ok(queryService.getTransactionSummary(companyId));
    }

    @GetMapping("/summary/total/{companyId}")
    public ResponseEntity<AccountingSummary> getTotalSummary(
            @PathVariable String companyId) {
        return ResponseEntity.ok(queryService.getTotalTransactionSummary(companyId));
    }

    @GetMapping("/unclassified")
    public ResponseEntity<List<UnclassifiedTransactionResponse>> getUnclassifiedTransactions(
            @RequestParam String companyId) {
        return ResponseEntity.ok(queryService.getUnclassifiedTransactions(companyId));
    }

}


package com.oncomm.oncomm.controller;


import com.oncomm.oncomm.application.AccountingUseCase;
import com.oncomm.oncomm.dto.request.AccountingProcessRequest;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounting")
@RequiredArgsConstructor
public class AccountingController {

    private final AccountingUseCase accountingUseCase;

    @PostMapping("/process")
    public ResponseEntity<String> processAccounting(@ModelAttribute AccountingProcessRequest request) {
        accountingUseCase.process(request.getTransactions(), request.getRules());
        return ResponseEntity.ok("처리 완료");
    }


    @GetMapping("/records")
    public ResponseEntity<List<ClassifiedTransactionResponse>> getRecords(
            @RequestParam("companyId") String companyId
    ) {
        return ResponseEntity.ok(accountingUseCase.getClassifiedTransactions(companyId));
    }
}

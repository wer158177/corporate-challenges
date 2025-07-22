package com.oncomm.oncomm.application;

import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import com.oncomm.oncomm.service.AccountingService;
import com.oncomm.oncomm.util.mapper.RuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountingUseCaseImpl implements AccountingUseCase {

    private final AccountingService accountingService;

    @Override
    public void process(MultipartFile csvFile, MultipartFile jsonFile) {
        accountingService.processCsvAndJson(csvFile, jsonFile);
    }

    @Override
    public List<ClassifiedTransactionResponse> getClassifiedTransactions(String companyId) {
        return accountingService.getClassifiedByCompany(companyId)
                .stream()
                .map(RuleMapper::toDto)
                .toList();
    }
}

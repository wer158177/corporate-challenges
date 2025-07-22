package com.oncomm.oncomm.application;


import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface AccountingUseCase {

    void process(MultipartFile csvFile, MultipartFile jsonFile);

    List<ClassifiedTransactionResponse> getClassifiedTransactions(String companyId);
}
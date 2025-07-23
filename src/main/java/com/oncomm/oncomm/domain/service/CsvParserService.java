package com.oncomm.oncomm.domain.service;

import com.oncomm.oncomm.domain.model.Transaction;
import com.oncomm.oncomm.infrastructure.repository.TransactionRepository;
import com.opencsv.CSVParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvParserService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public List<Transaction> parseTransactions(MultipartFile csvFile) {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean skipHeader = true;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            CSVParser parser = new CSVParser();

            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                String[] values = parser.parseLine(line);
                Transaction tx = Transaction.builder()
                        .txDatetime(LocalDateTime.parse(values[0], formatter)) // ✅ 필드명 일치
                        .description(values[1])
                        .deposit(parseLong(values[2]))
                        .withdraw(parseLong(values[3]))
                        .balance(parseLong(values[4]))
                        .branchInfo(values[5])
                        .createdAt(LocalDateTime.now())  // ✅ 생성 시점 기록
                        .build();

                transactions.add(transactionRepository.save(tx));
            }
        } catch (Exception e) {
            log.error("CSV 파싱 오류", e);
            throw new RuntimeException("CSV 파싱 중 오류 발생", e);
        }
        return transactions;
    }

    private Long parseLong(String value) {
        try {
            return value == null || value.isBlank() ? null : Long.parseLong(value.replaceAll(",", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}


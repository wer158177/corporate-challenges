package com.oncomm.oncomm.domain.service;

import com.oncomm.oncomm.domain.model.Transaction;
import com.oncomm.oncomm.exception.CsvParsingException;
import com.oncomm.oncomm.infrastructure.repository.TransactionRepository;
import com.opencsv.CSVParser;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service

public class CsvParserService {

    private final TransactionRepository transactionRepository;

    public CsvParserService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public List<Transaction> parseTransactions(MultipartFile csvFile) {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean skipHeader = true;
            int lineNumber = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            CSVParser parser = new CSVParser();

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                try {
                    String[] values = parser.parseLine(line);

                    if (values.length < 6) {
                        throw new CsvParsingException("필드 개수가 부족합니다 (6개 필요). 현재 라인: " + line);
                    }

                    Transaction tx = Transaction.builder()
                            .occurredAt(LocalDateTime.parse(values[0], formatter))
                            .description(values[1])
                            .deposit(parseLong(values[2], lineNumber))
                            .withdraw(parseLong(values[3], lineNumber))
                            .balance(parseLong(values[4], lineNumber))
                            .branchInfo(values[5])
                            .createdAt(LocalDateTime.now())
                            .build();

                    transactions.add(transactionRepository.save(tx));

                } catch (Exception parseEx) {
                    log.error("[CSV 파싱 실패] 라인 {}: {}", lineNumber, line, parseEx);
                    throw new CsvParsingException("CSV 파싱 실패 (라인 " + lineNumber + "): " + parseEx.getMessage(), parseEx);
                }
            }
        } catch (IOException io) {
            throw new CsvParsingException("CSV 파일 읽기 중 오류 발생", io);
        }

        return transactions;
    }

    private Long parseLong(String value, int lineNumber) {
        try {
            return value == null || value.isBlank() ? null : Long.parseLong(value.replaceAll(",", ""));
        } catch (NumberFormatException e) {
            throw new CsvParsingException("숫자 형식 오류 (라인 " + lineNumber + "): \"" + value + "\"", e);
        }
    }

}


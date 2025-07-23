package com.oncomm.oncomm;

import com.oncomm.oncomm.domain.model.Transaction;
import com.oncomm.oncomm.domain.service.CsvParserService;
import com.oncomm.oncomm.exception.CsvParsingException;
import com.oncomm.oncomm.infrastructure.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CsvParserServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CsvParserService csvParserService;

    @BeforeEach
    void setup() {
        // 공통적으로 저장 동작은 그대로 반환
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }
    private MockMultipartFile createCsv(String content) {
        return new MockMultipartFile("file", "test.csv", "text/csv", content.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void parseTransactions_정상작동() {
        String csv = """
                거래일시,적요,입금액,출금액,거래후잔액,거래점
                2025-07-20 13:45:11,스타벅스 강남2호점,0,5500,994500,강남지점
                """;

        List<Transaction> result = csvParserService.parseTransactions(createCsv(csv));

        assertThat(result).hasSize(1);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void parseTransactions_필드부족_예외() {
        String csv = """
                거래일시,적요,입금액,출금액,거래후잔액,거래점
                2025-07-20 13:45:11,스타벅스 강남2호점,0,5500
                """;

        assertThatThrownBy(() -> csvParserService.parseTransactions(createCsv(csv)))
                .isInstanceOf(CsvParsingException.class)
                .hasMessageContaining("필드 개수가 부족합니다");
    }

    @Test
    void parseTransactions_날짜형식오류() {
        String csv = """
                거래일시,적요,입금액,출금액,거래후잔액,거래점
                07-20-2025 13:45,스타벅스 강남2호점,0,5500,994500,강남지점
                """;

        assertThatThrownBy(() -> csvParserService.parseTransactions(createCsv(csv)))
                .isInstanceOf(CsvParsingException.class)
                .hasMessageContaining("CSV 파싱 실패");
    }

    @Test
    void parseTransactions_숫자형식오류() {
        String csv = """
                거래일시,적요,입금액,출금액,거래후잔액,거래점
                2025-07-20 13:45:11,스타벅스 강남2호점,0,fiveThousand,994500,강남지점
                """;

        assertThatThrownBy(() -> csvParserService.parseTransactions(createCsv(csv)))
                .isInstanceOf(CsvParsingException.class)
                .hasMessageContaining("숫자 형식 오류");
    }

    @Test
    void parseTransactions_빈숫자_허용() {
        String csv = """
                거래일시,적요,입금액,출금액,거래후잔액,거래점
                2025-07-20 13:45:11,스타벅스 강남2호점,,,994500,강남지점
                """;

        List<Transaction> result = csvParserService.parseTransactions(createCsv(csv));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDeposit()).isNull();
        assertThat(result.get(0).getWithdraw()).isNull();
    }


    @Test
    void parseTransactions_IOException_예외() throws IOException {
        MultipartFile badFile = mock(MultipartFile.class);
        when(badFile.getInputStream()).thenThrow(new IOException("Stream Error"));

        assertThatThrownBy(() -> csvParserService.parseTransactions(badFile))
                .isInstanceOf(CsvParsingException.class)
                .hasMessageContaining("CSV 파일 읽기 중 오류");
    }
}

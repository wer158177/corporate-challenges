package com.oncomm.oncomm.exception;

// com.oncomm.oncomm.exception.GlobalExceptionHandler

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CsvParsingException.class)
    public ResponseEntity<String> handleCsvParsing(CsvParsingException ex) {
        return ResponseEntity.badRequest().body("CSV 파일 오류: " + ex.getMessage());
    }

    @ExceptionHandler(JsonParsingException.class)
    public ResponseEntity<String> handleJsonParsing(JsonParsingException ex) {
        return ResponseEntity.badRequest().body("JSON 파일 오류: " + ex.getMessage());
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<String> handleCompanyNotFound(CompanyNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFound(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOther(Exception ex) {
        ex.printStackTrace(); // 또는 Logger 사용
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("알 수 없는 오류 발생: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
    }
}

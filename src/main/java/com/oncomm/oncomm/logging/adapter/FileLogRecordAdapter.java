package com.oncomm.oncomm.logging.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class FileLogRecordAdapter {

    public void logToFile(String message) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write(LocalDateTime.now() + " - " + message + "\n");
        } catch (IOException e) {
            log.error("파일 로그 기록 실패", e);
        }
    }
}
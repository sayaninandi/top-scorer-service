package com.intuit.topscorerservice.service.impl;

import com.intuit.topscorerservice.dto.PlayerScoreRequest;
import com.intuit.topscorerservice.service.PlayerScoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

@Slf4j
@Service
public class FileObserver {

    private static final String COMMA_DELIMITER = ",";
    @Value("${observe.folder.path:}")
    private String folderPath;

    @Value("${file.poll.interval}")
    private long pollingInterval;

    @Autowired
    private PlayerScoreService playerScoreService;

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void observe() throws Exception {
        log.info("observing {}", folderPath);
        File folder = new File(folderPath);

        if (!folder.exists()) {
            throw new RuntimeException("Directory not found: " + folder);
        }

        FileAlterationObserver observer = new FileAlterationObserver(folder);
        FileAlterationMonitor monitor = new FileAlterationMonitor(pollingInterval);
        FileAlterationListener listener = new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
                try {
                    log.info("File created: {}", file.getCanonicalPath());
                    try (Stream<String> lines = Files.lines(file.toPath())) {
                        lines.forEach(line -> {
                            PlayerScoreRequest request = getCreateRequest(line.split(COMMA_DELIMITER));
                            playerScoreService.saveScore(request);
                        });
                    }
                } catch (IOException e) {
                    log.error("Encounter error on file create event", e);
                }
            }

            private PlayerScoreRequest getCreateRequest(String[] lineData) {
                return PlayerScoreRequest.builder()
                        .playerId(lineData[0])
                        .gameId(lineData[1])
                        .score(Long.parseLong(lineData[2]))
                        .build();
            }
        };

        observer.addListener(listener);
        monitor.addObserver(observer);
        monitor.start();
    }

}

package com.intuit.topscorerservice.data;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

@Slf4j
public class CreateDbSchema {

    private String baseDir;

    private static final String schemaPath = "schema";


    public void prepareSchema(MySQLContainer MYSQL_CONTAINER) throws IOException {
        baseDir = STR."\{System.getProperty("user.dir")}/";
        JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(MYSQL_CONTAINER, "");
        Files.walk(Paths.get(baseDir + schemaPath))
                .filter(f -> f.toString().endsWith(".sql"))
                .sorted(Comparator.comparingInt(this::pathToInt))
                .map(path -> {
                    log.info("Applying: {}", path);
                    try {
                        return Files.readAllBytes(path);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .map(String::new)
                .forEach(sql -> {
                    try {
                        ScriptUtils.executeDatabaseScript(jdbcDatabaseDelegate, "", sql);
                    } catch (ScriptException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


    private int pathToInt(final Path path) {
        try {
            return Integer.parseInt(path.getFileName().toString().replace(".sql", ""));
        } catch (final NumberFormatException nfe) {
            return 1000;
        }
    }

}

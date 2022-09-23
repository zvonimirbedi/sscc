package com.example.sscc.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
@Slf4j
public class FileWriter {

    // could be parameterised
    private final Path path = Path.of("./SSCC_out.txt");
    public void writeToLocalFile(String text){
        // write to file
        try {
            if(!Files.exists(path))
                Files.createFile(path);
            Files.write(path, (text + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            log.info("Writing to file finished.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

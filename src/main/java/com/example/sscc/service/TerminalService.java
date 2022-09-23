package com.example.sscc.service;

import com.example.sscc.model.Feed;
import com.example.sscc.model.SsccModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Scanner;

@Service
@Slf4j
public class TerminalService {

    public Flux<SsccModel> terminalStart() {
        return Flux.from(sink -> {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                sink.onNext(new SsccModel(scanner.nextLine(), false, Feed.CLI));
            }});
    }

}
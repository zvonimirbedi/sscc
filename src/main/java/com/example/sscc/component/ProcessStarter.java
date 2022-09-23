package com.example.sscc.component;

import com.example.sscc.service.SocketService;
import com.example.sscc.service.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class ProcessStarter {

    private final SocketService socketService;
    private final TerminalService terminalService;
    private final ValidationSSCC validationSSCC;
    private final FileWriter fileWriter;

    public ProcessStarter(SocketService socketService, TerminalService terminalService, ValidationSSCC validationSSCC, FileWriter fileWriter) {
        this.socketService = socketService;
        this.terminalService = terminalService;
        this.validationSSCC = validationSSCC;
        this.fileWriter = fileWriter;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void terminalFlux() {
        // to add another source, merge new flux to existing one
        Flux.merge(
                socketService.socketStart(),
                terminalService.terminalStart())
                .filter(sscc -> sscc.isValidated() || validationSSCC.isValidateSSCC(sscc.getCode()))
                .doOnNext(sscc -> {
                    sscc.setValidated(true);
                    sscc.setCode(sscc.getCode().trim());
                    log.info("Writing code: " + sscc.getCode() + " from source: " + sscc.getFeed());
                    fileWriter.writeToLocalFile(sscc.getCode());
                })
                .subscribe();
    }
}

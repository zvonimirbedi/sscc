package com.example.sscc.service;

import com.example.sscc.component.ValidationSSCC;
import com.example.sscc.model.Feed;
import com.example.sscc.model.SsccModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpServer;

@Service
@Slf4j
public class SocketService {

    private final ValidationSSCC validationSSCC;

    public SocketService(ValidationSSCC validationSSCC) {
        this.validationSSCC = validationSSCC;
    }

    public Flux<SsccModel> socketStart() {
        // https://projectreactor.io/docs/netty/release/reference/index.html
        return Flux.from(sink ->
            TcpServer.create()
                    // could be parameterised
                    .port(3038)
                    .handle((inbound, outbound) -> inbound.receive().asString()
                            .filter(validationSSCC::isValidateSSCC)
                            .doOnNext(message -> {
                                outbound.sendString(Mono.just(message)).then().subscribe();
                                // outbound.sendFile(Path.of("")).then().subscribe();
                                sink.onNext(new SsccModel(message, true, Feed.SOCKET));
                            })
                            .then())
                    .bindNow()
                    .onDispose()
                    .subscribe()
        );
    }
}

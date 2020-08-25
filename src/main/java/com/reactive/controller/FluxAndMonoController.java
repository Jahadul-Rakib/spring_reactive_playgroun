package com.reactive.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    @GetMapping("/api/mono")
    public Mono<Integer> helloMono() {
        return Mono.just(1);
    }

    @GetMapping("/api/flux")
    public Flux<Integer> helloFlux() {
        return Flux.just(1, 2, 3);
    }

    @GetMapping(value = "/api/flux-stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> helloFluxOther() {
        return Flux.interval(Duration.ofSeconds(1)).log();
    }


}

package com.reactive.playground;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
public class FluxUnitTest {

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("Rakib", "Dilruba", "SomeOne");
        stringFlux.subscribe(System.out::println);
    }

    @Test
    public void fluxErrorTest() {
        Flux<String> stringFlux = Flux.just("Rakib", "Dilruba", "SomeOne")
                .concatWith(Flux.error(new RuntimeException()))
                .concatWith(Flux.just("After Error"))
                .log();
        stringFlux.subscribe(
                System.out::println,
                (e) -> System.out.println("Error is " + e),
                () -> System.out.println("Completed"));
    }

    @Test
    public void fluxWithoutErrorTest() {
        Flux<String> stringFlux = Flux.just("Rakib", "Dilruba", "SomeOne")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Rakib")
                .expectNext("Dilruba")
                .expectNext("SomeOne")
                .verifyComplete();

    }

    @Test
    public void fluxWithErrorTest() {
        Flux<String> stringFlux = Flux.just("Rakib", "Dilruba", "SomeOne")
                .concatWith(Flux.error(new RuntimeException("Runtime Exception")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Rakib")
                .expectNext("Dilruba")
                .expectNext("SomeOne")
                .expectError(RuntimeException.class)
                .verify();

    }

    @Test
    public void fluxTestElementCount() {
        Flux<String> stringFlux = Flux.just("Rakib", "Dilruba", "SomeOne")
                .concatWith(Flux.error(new RuntimeException("Runtime Exception")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectError(RuntimeException.class)
                .verify();

    }

}

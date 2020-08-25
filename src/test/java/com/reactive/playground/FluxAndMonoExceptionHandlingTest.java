package com.reactive.playground;

import com.reactive.utils.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
public class FluxAndMonoExceptionHandlingTest {
    @Test
    public void fluxErrorHandling_onErrorResume() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception from")))
                .concatWith(Flux.just("D"))
                .onErrorResume((e) -> {
                    System.out.println(e);
                    return Flux.just("Error");
                });

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("Error")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorReturn() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception from")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("Error");

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("Error")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorMap() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception from")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e));

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_onErrorMap_With_Retry() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception from")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .retry(2);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_onErrorMap_With_Retry_BackOff() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception from")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .retryBackoff(2, Duration.ofSeconds(5));

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }
}

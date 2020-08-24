package com.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest() {
        Flux<Integer> range = Flux.range(1, 10)
                .log();
        StepVerifier.create(range)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenRequest(1)
                .expectNext(3)
                .thenRequest(1)
                .expectNext(4)
                .thenCancel()
                .verify();

    }

    @Test
    public void backPressure() {
        Flux<Integer> range = Flux.range(1, 10).log();
        range.subscribe(
                (element) -> System.out.println(element),
                (error) -> System.out.println("Error is " + error),
                () -> System.out.println("Done"),
                (subscription -> subscription.request(3))
        );
    }

    @Test
    public void backPressure_Cancel() {
        Flux<Integer> range = Flux.range(1, 10).log();
        range.subscribe(
                (element) -> System.out.println(element),
                (error) -> System.out.println("Error is " + error),
                () -> System.out.println("Done"),
                (subscription -> subscription.cancel())
        );
    }

    @Test
    public void backPressure_Customize() {
        Flux<Integer> range = Flux.range(1, 10).log();
        range.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println(value);
                if (value == 4) {
                    cancel();
                }
            }
        });
    }
}

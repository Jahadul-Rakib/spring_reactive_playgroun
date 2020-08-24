package com.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1)).log();
        interval.subscribe(System.out::println);
        Thread.sleep(300);
    }

    @Test
    public void infiniteSequenceTest() throws InterruptedException {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(100))
                .take(3)
                .log();

        StepVerifier.create(interval.log())
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    public void infiniteSequence_MapTest() throws InterruptedException {
        Flux<Integer> interval = Flux.interval(Duration.ofMillis(100))
                .map(aLong -> aLong.intValue())
                .take(3)
                .log();

        StepVerifier.create(interval.log())
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }

    @Test
    public void infiniteSequence_Map_with_dely_Test() throws InterruptedException {
        Flux<Integer> interval = Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofSeconds(1))
                .map(aLong -> aLong.intValue())
                .take(3)
                .log();

        StepVerifier.create(interval.log())
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }
}

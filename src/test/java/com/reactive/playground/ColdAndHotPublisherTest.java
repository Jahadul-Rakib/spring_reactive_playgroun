package com.reactive.playground;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootTest
public class ColdAndHotPublisherTest {
    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E")
                .delayElements(Duration.ofSeconds(1));
        stringFlux.subscribe(s -> System.out.println("First :"+s));
        Thread.sleep(2000);
        stringFlux.subscribe(s -> System.out.println("Second :"+s));
        Thread.sleep(4000);
    }

    @Test
    public void hotPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E")
                .delayElements(Duration.ofSeconds(1));
        ConnectableFlux<String> flux = stringFlux.publish();
        flux.connect();
        flux.subscribe(s -> System.out.println("First :"+s));
        Thread.sleep(2000);
        flux.subscribe(s -> System.out.println("Second :"+s));
        Thread.sleep(4000);
    }
}

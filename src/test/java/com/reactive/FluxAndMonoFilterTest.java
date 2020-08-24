package com.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class FluxAndMonoFilterTest {


    @Test
    public void fluxUsingIterable() {
        List<String> list = Arrays.asList("Apple", "Banana", "Papa", "Mango");

        Flux<String> fromIterable = Flux.fromIterable(list)
                .filter(s -> s.endsWith("a")).log();
        StepVerifier.create(fromIterable)
                .expectNext("Banana", "Papa")
                .verifyComplete();
    }

}

package com.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@SpringBootTest
public class FluxAndMonoFactoryTest {


    @Test
    public void fluxUsingIterable() {
        List<String> list = Arrays.asList("Apple", "Banana", "Papa", "Mango");

        Flux<String> fromIterable = Flux.fromIterable(list);
        StepVerifier.create(fromIterable)
                .expectNext("Apple", "Banana", "Papa", "Mango")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        String[] list = new String[]{"Apple", "Banana", "Papa", "Mango"};

        Flux<String> fromIterable = Flux.fromArray(list);

        StepVerifier.create(fromIterable)
                .expectNext("Apple", "Banana", "Papa", "Mango")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStram() {
        List<String> list = Arrays.asList("Apple", "Banana", "Papa", "Mango");

        Flux<String> fromIterable = Flux.fromStream(list.stream());

        StepVerifier.create(fromIterable)
                .expectNext("Apple", "Banana", "Papa", "Mango")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrNull() {
        Mono<String> objectMono = Mono.justOrEmpty(null);

        StepVerifier.create(objectMono)
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier() {
        Supplier<String> supplier = () -> "Rakib";
        Mono<String> fromSupplier = Mono.fromSupplier(supplier);
        System.out.println(fromSupplier.log());

        StepVerifier.create(fromSupplier)
                .expectNext("Rakib")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange() {

        Flux<Integer> range = Flux.range(1, 3);
        StepVerifier.create(range)
                .expectNext(1,2,3)
                .verifyComplete();
    }
}

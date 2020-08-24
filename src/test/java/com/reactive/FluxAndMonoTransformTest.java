package com.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

@SpringBootTest
public class FluxAndMonoTransformTest {


    @Test
    public void fluxTransformUsing_Map() {
        List<String> list = Arrays.asList("Apple", "Banana", "Papa", "Mango");

        Flux<String> fromIterable = Flux.fromIterable(list)
                .map(s -> s.toUpperCase())
                .log();
        StepVerifier.create(fromIterable)
                .expectNext("APPLE", "BANANA", "PAPA", "MANGO")
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsing_Map_With_lengt() {
        List<String> list = Arrays.asList("Apple", "Banana", "Papa", "Mango");

        Flux<Integer> fromIterable = Flux.fromIterable(list)
                .map(s -> s.length())
                .log();
        StepVerifier.create(fromIterable)
                .expectNext(5, 6, 4, 5)
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsing_Map_With_lengt_repeat() {
        List<String> list = Arrays.asList("Apple", "Banana", "Papa", "Mango");

        Flux<Integer> fromIterable = Flux.fromIterable(list)
                .map(s -> s.length())
                .repeat(1)
                .log();
        StepVerifier.create(fromIterable)
                .expectNext(5, 6, 4, 5, 5, 6, 4, 5)
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsing_Map_With_Filter() {
        List<String> list = Arrays.asList("Apple", "Banana", "Papa", "Mango");

        Flux<String> fromIterable = Flux.fromIterable(list)
                .filter(s -> s.length() > 4)
                .map(s -> s.toUpperCase())
                .log();
        StepVerifier.create(fromIterable)
                .expectNext("APPLE", "BANANA", "MANGO")
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsing_FlatMap() {
        List<String> list = Arrays.asList("Apple", "Banana", "Papa", "Mango");

        Flux<String> fromIterable = Flux.fromIterable(list)
                .flatMap(s -> {
                    return Flux.fromIterable(addString(s)); //return db or external service call s -> Flux<String>
                });
        StepVerifier.create(fromIterable)
                .expectNextCount(4)
                .verifyComplete();
    }


    @Test
    public void fluxTransformUsing_FlatMap_Window() {
        List<String> list = Arrays.asList("Apple", "Banana", "Papa", "Mango");

        Flux<String> fromIterable = Flux.fromIterable(list)
                .window(2) //(A,B),(C,D)
                .flatMap((s) -> s.map(s1 -> addString(s1)).subscribeOn(parallel()))
                .flatMap(strings -> Flux.fromIterable(strings));
        StepVerifier.create(fromIterable)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsing_ConcatMap_Window_Maintain_Order() {
        List<String> list = Arrays.asList("Apple", "Banana", "Papa", "Mango");

        Flux<String> fromIterable = Flux.fromIterable(list)
                .window(2) //(A,B),(C,D)
                .concatMap((s) -> s.map(s1 -> addString(s1)).subscribeOn(parallel())) //maintain order
                .flatMap(strings -> Flux.fromIterable(strings));
        StepVerifier.create(fromIterable)
                .expectNextCount(4)
                .verifyComplete();
    }


    private List<String> addString(String s) {
        return Arrays.asList(s.concat(" Green"));
    }
}

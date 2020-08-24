package com.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class MonoUnitTest {

    @Test
    public void monoTest(){
        Mono<String> mono = Mono.just("Rakib");
        StepVerifier.create(mono)
                .expectNext("Rakib")
                .verifyComplete();
    }

    @Test
    public void monoTest_Error(){

        StepVerifier.create(Mono.error(new RuntimeException()))
                .expectError(RuntimeException.class)
                .verify();
    }

}

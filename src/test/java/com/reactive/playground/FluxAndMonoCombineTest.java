package com.reactive.playground;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
public class FluxAndMonoCombineTest {
    @Test
    public void combineUsing_Marge(){

        Flux<String> stringFluxA = Flux.just("A", "B", "C", "D", "E", "F");
        Flux<String> stringFluxB = Flux.just("A1", "B1", "C1", "D1", "E1", "F1");

        Flux<String> merge = Flux.merge(stringFluxA, stringFluxB);

        StepVerifier.create(merge)
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F", "A1", "B1", "C1", "D1", "E1", "F1")
                .verifyComplete();
    }

    @Test
    public void combineUsing_Marge_With_Dely(){

        Flux<String> stringFluxA = Flux.just("A", "B", "C", "D", "E", "F").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFluxB = Flux.just("A1", "B1", "C1", "D1", "E1", "F1").delayElements(Duration.ofSeconds(1));;

        Flux<String> merge = Flux.merge(stringFluxA, stringFluxB);

        StepVerifier.create(merge.log())
                .expectSubscription()
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void combineUsing_Combine_With_Dely(){

        Flux<String> stringFluxA = Flux.just("A", "B", "C", "D", "E", "F").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFluxB = Flux.just("A1", "B1", "C1", "D1", "E1", "F1").delayElements(Duration.ofSeconds(1));;

        Flux<String> merge = Flux.concat(stringFluxA, stringFluxB); // for maintaining order

        StepVerifier.create(merge.log())
                .expectSubscription()
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void combineUsing_Combine_Element(){

        Flux<String> stringFluxA = Flux.just("A", "B", "C", "D", "E", "F").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFluxB = Flux.just("A1", "B1", "C1", "D1", "E1", "F1").delayElements(Duration.ofSeconds(1));;

        Flux<String> merge = Flux.zip(stringFluxA, stringFluxB, (t1,t2)->{
            return t1.concat(t2);
        }); // for (A,A1),(B,B1) this formate

        StepVerifier.create(merge.log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();
    }
}

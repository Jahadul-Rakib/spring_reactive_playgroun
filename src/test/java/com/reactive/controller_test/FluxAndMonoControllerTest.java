package com.reactive.controller_test;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebFluxTest
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient client;


    @Test
    public void fluxControllerUnitTestApproachOne() {
        Flux<Integer> responseBody = client.get().uri("/api/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(responseBody.log())
                .expectSubscription()
                .expectNext(1, 2, 3)
                .verifyComplete();
    }

    @Test
    public void fluxControllerUnitTestApproachTwo() {
        client.get().uri("/api/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .hasSize(3);
    }

    @Test
    public void fluxControllerUnitTestApproachThree() {
        EntityExchangeResult<List<Integer>> responseBody = client.get().uri("/api/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();

        List<Integer> list = Arrays.asList(1, 2, 3);

        assertEquals(list, responseBody.getResponseBody());
    }

    @Test
    public void fluxControllerUnitTestApproachFour() {
        List<Integer> list = Arrays.asList(1, 2, 3);

        client.get().uri("/api/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith((result) -> {
                    assertEquals(list, result.getResponseBody());
                });

    }


    @Test
    public void fluxControllerStream() {
        Flux<Long> responseBody = client.get().uri("/api/flux-stream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(0l, 1l, 2l, 3l)
                .thenCancel()
                .verify();
    }


    @Test
    public void mono() {
        client.get().uri("/api/mono")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(integerEntityExchangeResult -> assertEquals(1, integerEntityExchangeResult.getResponseBody()));
    }


}
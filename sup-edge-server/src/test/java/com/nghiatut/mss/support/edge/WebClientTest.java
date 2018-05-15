package com.nghiatut.mss.support.edge;

import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientTest {

    WebClient webClient;

    @Test
    public void call() {
        WebClient.builder()
                .baseUrl("http://")
                .build()
                .get()
        .uri("")
        ;
    }

}

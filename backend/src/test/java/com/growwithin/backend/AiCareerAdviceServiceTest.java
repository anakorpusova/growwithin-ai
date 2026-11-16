package com.growwithin.backend;

import com.sun.net.httpserver.HttpServer;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;

class AiCareerAdviceServiceTest {

    @Test
    void returnsFallbackWhenGeminiTimesOut() throws Exception {
        HttpServer server = HttpServer.create(
                new InetSocketAddress("127.0.0.1", 0), 0);

        server.createContext("/", exchange -> {
            try {
                Thread.sleep(1500);
                byte[] response = "{}".getBytes();
                exchange.sendResponseHeaders(200, response.length);
                try (var output = exchange.getResponseBody()) {
                    output.write(response);
                }
            } catch (Exception ignored) {
                exchange.close();
            }
        });

        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();

        try {
            AiCareerAdviceService service =
                    new AiCareerAdviceService("test-key", "test-model");

            var httpClient = java.net.http.HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(1))
                    .build();

            var factory = new JdkClientHttpRequestFactory(httpClient);
            factory.setReadTimeout(Duration.ofMillis(100));

            RestClient testClient = RestClient.builder()
                    .baseUrl("http://127.0.0.1:" + server.getAddress().getPort())
                    .requestFactory(factory)
                    .build();

            Field field = AiCareerAdviceService.class
                    .getDeclaredField("restClient");
            field.setAccessible(true);
            field.set(service, testClient);

            AiAdvice advice = service.generateAdvice(
                    List.of(),
                    List.of()
            );

            assertFalse(advice.aiGenerated());
            assertEquals(
                    "The AI recommendation service is temporarily unavailable.",
                    advice.recommendation()
            );
        } finally {
            server.stop(0);
        }
    }
}

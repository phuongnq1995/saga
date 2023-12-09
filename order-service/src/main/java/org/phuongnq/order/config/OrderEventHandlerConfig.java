package org.phuongnq.order.config;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.OrchestratorRequestDTO;
import org.phuongnq.dto.response.OrchestratorResponseDTO;
import org.phuongnq.order.service.OrderEventUpdateService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class OrderEventHandlerConfig {
    private final Flux<OrchestratorRequestDTO> flux;
    private final OrderEventUpdateService service;

    @Bean
    public Supplier<Flux<OrchestratorRequestDTO>> supplier() {
        return () -> flux;
    }

    @Bean
    public Consumer<Flux<OrchestratorResponseDTO>> consumer() {
        return f -> f
                .doOnNext(c -> System.out.println("Consuming :: " + c))
                .flatMap(service::updateOrder)
                .subscribe();
    }

}

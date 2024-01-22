package org.phuongnq.saga.config;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.OrchestratorRequestDTO;
import org.phuongnq.dto.response.OrchestratorResponseDTO;
import org.phuongnq.saga.service.OrchestratorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class OrchestratorConfig {
    private final OrchestratorService orchestratorService;

    @Bean
    public Function<Flux<OrchestratorRequestDTO>, Flux<OrchestratorResponseDTO>> processor() {
        return flux -> flux
                .flatMap(orchestratorService::orderProduct);
    }
}

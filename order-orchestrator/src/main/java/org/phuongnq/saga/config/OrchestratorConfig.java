package org.phuongnq.saga.config;

import org.phuongnq.dto.request.OrchestratorRequestDTO;
import org.phuongnq.dto.response.OrchestratorResponseDTO;
import org.phuongnq.saga.service.OrchestratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class OrchestratorConfig {

    @Autowired
    private OrchestratorService orchestratorService;

    @Bean
    public Function<Flux<OrchestratorRequestDTO>, Flux<OrchestratorResponseDTO>> processor() {
        return flux -> flux
                .flatMap(dto -> this.orchestratorService.orderProduct(dto))
                .doOnNext(dto -> System.out.println("Response : " + dto.toString()));
    }

}

package org.phuongnq.saga.service.steps;

import org.phuongnq.dto.request.InventoryRequestDTO;
import org.phuongnq.dto.response.InventoryResponseDTO;
import org.phuongnq.enums.InventoryStatus;
import org.phuongnq.saga.service.WorkflowStep;
import org.phuongnq.saga.service.WorkflowStepStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class InventoryStep implements WorkflowStep {
    private static final Logger log = LoggerFactory.getLogger(InventoryStep.class);
    private final WebClient webClient;
    private final InventoryRequestDTO requestDTO;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    public InventoryStep(WebClient webClient, InventoryRequestDTO requestDTO) {
        this.webClient = webClient;
        this.requestDTO = requestDTO;
    }

    @Override
    public WorkflowStepStatus getStatus() {
        return this.stepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        var a=  this.webClient
                .post()
                .uri("/inventory/deduct")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(InventoryResponseDTO.class)
                .map(r -> r.getStatus().equals(InventoryStatus.AVAILABLE))
                .doOnNext(b -> this.stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
        System.out.println();
        return a;
    }

    @Override
    public Mono<Boolean> revert() {
        log.info("Revert Inventory {}", requestDTO);
        return this.webClient
                .post()
                .uri("/inventory/add")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(r -> true)
                .onErrorReturn(false);
    }
}

package org.phuongnq.saga.service;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.InventoryRequestDTO;
import org.phuongnq.dto.request.OrchestratorRequestDTO;
import org.phuongnq.dto.request.PaymentRequestDTO;
import org.phuongnq.dto.response.OrchestratorResponseDTO;
import org.phuongnq.enums.OrderStatus;
import org.phuongnq.saga.service.steps.InventoryStep;
import org.phuongnq.saga.service.steps.PaymentStep;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrchestratorService {

    @Qualifier("payment")
    private final WebClient paymentClient;

    @Qualifier("inventory")
    private final WebClient inventoryClient;

    public Mono<OrchestratorResponseDTO> orderProduct(final OrchestratorRequestDTO requestDTO) {
        Workflow orderWorkflow = this.getOrderWorkflow(requestDTO);
        return Flux.fromStream(() -> orderWorkflow.getSteps().stream())
                .flatMap(WorkflowStep::process)
                .handle(((aBoolean, synchronousSink) -> {
                    if (aBoolean)
                        synchronousSink.next(true);
                    else
                        synchronousSink.error(new WorkflowException("create order failed!"));
                }))
                .then(Mono.fromCallable(() -> getResponseDTO(requestDTO, OrderStatus.ORDER_COMPLETED)))
                .onErrorResume(ex -> this.revertOrder(orderWorkflow, requestDTO));

    }

    private Mono<OrchestratorResponseDTO> revertOrder(final Workflow workflow, final OrchestratorRequestDTO requestDTO) {
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .filter(wf -> wf.getStatus().equals(WorkflowStepStatus.COMPLETE))
                .flatMap(WorkflowStep::revert)
                .retry(3)
                .then(Mono.just(this.getResponseDTO(requestDTO, OrderStatus.ORDER_CANCELLED)));
    }

    private Workflow getOrderWorkflow(OrchestratorRequestDTO requestDTO) {
        WorkflowStep paymentStep = new PaymentStep(this.paymentClient, this.getPaymentRequestDTO(requestDTO));
        WorkflowStep inventoryStep = new InventoryStep(this.inventoryClient, this.getInventoryRequestDTO(requestDTO));
        return new OrderWorkflow(List.of(paymentStep, inventoryStep));
    }

    private OrchestratorResponseDTO getResponseDTO(OrchestratorRequestDTO requestDTO, OrderStatus status) {
        OrchestratorResponseDTO responseDTO = new OrchestratorResponseDTO();
        responseDTO.setOrderId(requestDTO.getOrderId());
        responseDTO.setAmount(requestDTO.getAmount());
        responseDTO.setProductId(requestDTO.getProductId());
        responseDTO.setUserId(requestDTO.getUserId());
        responseDTO.setStatus(status);
        return responseDTO;
    }

    private PaymentRequestDTO getPaymentRequestDTO(OrchestratorRequestDTO requestDTO) {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setUserId(requestDTO.getUserId());
        paymentRequestDTO.setAmount(requestDTO.getAmount());
        paymentRequestDTO.setOrderId(requestDTO.getOrderId());
        return paymentRequestDTO;
    }

    private InventoryRequestDTO getInventoryRequestDTO(OrchestratorRequestDTO requestDTO) {
        InventoryRequestDTO inventoryRequestDTO = new InventoryRequestDTO();
        inventoryRequestDTO.setUserId(requestDTO.getUserId());
        inventoryRequestDTO.setProductId(requestDTO.getProductId());
        inventoryRequestDTO.setOrderId(requestDTO.getOrderId());
        return inventoryRequestDTO;
    }

}

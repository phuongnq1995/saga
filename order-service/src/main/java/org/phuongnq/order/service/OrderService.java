package org.phuongnq.order.service;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.OrchestratorRequestDTO;
import org.phuongnq.dto.request.OrderRequestDTO;
import org.phuongnq.dto.response.OrderResponseDTO;
import org.phuongnq.enums.OrderStatus;
import org.phuongnq.order.entity.PurchaseOrder;
import org.phuongnq.order.repository.PurchaseOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;

@Service
@Observed(name = "orderService")
@RequiredArgsConstructor
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    // product price map
    private static final Map<Integer, Double> PRODUCT_PRICE = Map.of(
            1, 100d,
            2, 200d,
            3, 300d
    );

    private final Tracer tracer;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final Sinks.Many<OrchestratorRequestDTO> sink;

    public Mono<PurchaseOrder> createOrder(OrderRequestDTO orderRequestDTO) {
        log.info("Creating order <{}>", orderRequestDTO.toString());
        return purchaseOrderRepository.save(dtoToEntity(orderRequestDTO))
                .doOnNext(e -> {
                    orderRequestDTO.setOrderId(e.getId());
                    orderRequestDTO.setTraceId(tracer.currentSpan().context().traceId());
                })
                .doOnNext(e -> emitEvent(orderRequestDTO));
    }

    public Flux<OrderResponseDTO> getAll() {
        return purchaseOrderRepository.findAll()
                .map(this::entityToDto);
    }

    private void emitEvent(OrderRequestDTO orderRequestDTO) {
        sink.tryEmitNext(getOrchestratorRequestDTO(orderRequestDTO));
    }

    private PurchaseOrder dtoToEntity(final OrderRequestDTO dto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(dto.getOrderId());
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(PRODUCT_PRICE.get(purchaseOrder.getProductId()));
        return purchaseOrder;
    }

    private OrderResponseDTO entityToDto(final PurchaseOrder purchaseOrder) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(purchaseOrder.getId());
        dto.setProductId(purchaseOrder.getProductId());
        dto.setUserId(purchaseOrder.getUserId());
        dto.setStatus(purchaseOrder.getStatus());
        dto.setAmount(purchaseOrder.getPrice());
        return dto;
    }

    public OrchestratorRequestDTO getOrchestratorRequestDTO(OrderRequestDTO orderRequestDTO) {
        OrchestratorRequestDTO requestDTO = new OrchestratorRequestDTO();
        requestDTO.setUserId(orderRequestDTO.getUserId());
        requestDTO.setAmount(PRODUCT_PRICE.get(orderRequestDTO.getProductId()));
        requestDTO.setOrderId(orderRequestDTO.getOrderId());
        requestDTO.setProductId(orderRequestDTO.getProductId());
        return requestDTO;
    }

}

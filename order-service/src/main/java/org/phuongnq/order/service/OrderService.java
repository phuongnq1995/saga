package org.phuongnq.order.service;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.OrchestratorRequestDTO;
import org.phuongnq.dto.request.OrderRequestDTO;
import org.phuongnq.dto.response.OrderResponseDTO;
import org.phuongnq.enums.OrderStatus;
import org.phuongnq.order.entity.Product;
import org.phuongnq.order.entity.PurchaseOrder;
import org.phuongnq.order.repository.ProductRepository;
import org.phuongnq.order.repository.PurchaseOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final Sinks.Many<OrchestratorRequestDTO> sink;

    public Mono<PurchaseOrder> createOrder(OrderRequestDTO orderRequestDTO) {
        log.info("Creating order <{}>", orderRequestDTO.toString());
        return productRepository.findById(orderRequestDTO.getProductId())
                .map(product -> dtoToEntity(orderRequestDTO, product.getPrice()))
                .flatMap(purchaseOrderRepository::save)
                .doOnNext(e -> orderRequestDTO.setOrderId(e.getId()))
                .doOnNext(e -> emitEvent(orderRequestDTO, e.getPrice()));
    }

    public Flux<OrderResponseDTO> getAllOrders() {
        return purchaseOrderRepository.findAll()
                .map(this::entityToDto);
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    private void emitEvent(OrderRequestDTO orderRequestDTO, Double price) {
        sink.tryEmitNext(getOrchestratorRequestDTO(orderRequestDTO, price));
    }

    private PurchaseOrder dtoToEntity(final OrderRequestDTO dto, Double price) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(dto.getOrderId());
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(price);
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

    public OrchestratorRequestDTO getOrchestratorRequestDTO(OrderRequestDTO orderRequestDTO, Double price) {
        OrchestratorRequestDTO requestDTO = new OrchestratorRequestDTO();
        requestDTO.setUserId(orderRequestDTO.getUserId());
        requestDTO.setAmount(price);
        requestDTO.setOrderId(orderRequestDTO.getOrderId());
        requestDTO.setProductId(orderRequestDTO.getProductId());
        return requestDTO;
    }

    public Mono<Void> cleanData() {
        return purchaseOrderRepository.deleteAll();
    }
}

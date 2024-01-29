package org.phuongnq.order.controller;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.OrderRequestDTO;
import org.phuongnq.dto.response.OrderResponseDTO;
import org.phuongnq.order.entity.PurchaseOrder;
import org.phuongnq.order.service.OrderService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("/create")
    public Mono<PurchaseOrder> createOrder(@RequestBody Mono<OrderRequestDTO> mono) {
        return mono.flatMap(service::createOrder);
    }

    @GetMapping("/all")
    public Flux<OrderResponseDTO> getOrders() {
        return service.getAll();
    }

    @DeleteMapping
    public Mono<Void> cleanData() {
        return service.cleanData();
    }
}

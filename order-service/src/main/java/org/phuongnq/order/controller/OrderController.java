package org.phuongnq.order.controller;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.OrderRequestDTO;
import org.phuongnq.dto.response.OrderResponseDTO;
import org.phuongnq.order.entity.Product;
import org.phuongnq.order.entity.PurchaseOrder;
import org.phuongnq.order.service.OrderService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("/order")
    public Mono<PurchaseOrder> createOrder(@RequestBody Mono<OrderRequestDTO> mono) {
        return mono.flatMap(service::createOrder);
    }

    @GetMapping("/order")
    public Flux<OrderResponseDTO> getOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/products")
    public Flux<Product> getProducts() {
        return service.getAllProducts();
    }

    @DeleteMapping
    public Mono<Void> cleanData() {
        return service.cleanData();
    }
}

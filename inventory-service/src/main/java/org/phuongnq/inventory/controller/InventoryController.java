package org.phuongnq.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.InventoryRequestDTO;
import org.phuongnq.dto.response.InventoryResponseDTO;
import org.phuongnq.inventory.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @PostMapping("/deduct")
    public Mono<InventoryResponseDTO> deduct(@RequestBody final InventoryRequestDTO requestDTO) {
        return service.deductInventory(requestDTO);
    }

    @PostMapping("/add")
    public Mono<Void> add(@RequestBody final InventoryRequestDTO requestDTO) {
        return service.addInventory(requestDTO);
    }
}

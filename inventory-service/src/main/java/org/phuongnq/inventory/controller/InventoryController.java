package org.phuongnq.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.InventoryRequestDTO;
import org.phuongnq.dto.response.InventoryResponseDTO;
import org.phuongnq.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @PostMapping("/deduct")
    public ResponseEntity<InventoryResponseDTO> deduct(@RequestBody final InventoryRequestDTO requestDTO) {
        return ResponseEntity.ok(service.deductInventory(requestDTO));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody final InventoryRequestDTO requestDTO) {
        service.addInventory(requestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> cleanData() {
        service.cleanData();
        return ResponseEntity.ok().build();
    }
}

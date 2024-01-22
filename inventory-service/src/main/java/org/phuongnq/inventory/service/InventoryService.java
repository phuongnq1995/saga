package org.phuongnq.inventory.service;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.InventoryRequestDTO;
import org.phuongnq.dto.response.InventoryResponseDTO;
import org.phuongnq.enums.InventoryStatus;
import org.phuongnq.inventory.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;

    public Mono<InventoryResponseDTO> deductInventory(final InventoryRequestDTO requestDTO) {
        return inventoryRepository.findById(requestDTO.getProductId())
                .flatMap(inventory -> {
                    if (inventory.getQuantity() > 0) {
                        inventory.setQuantity(inventory.getQuantity() - 1);
                        log.info("Deduct Inventory, Remaining: " + inventory);
                        return inventoryRepository.save(inventory);
                    }
                    throw new IllegalArgumentException();
                })
                .map(payment -> {
                    InventoryResponseDTO responseDTO = new InventoryResponseDTO();
                    responseDTO.setOrderId(requestDTO.getOrderId());
                    responseDTO.setUserId(requestDTO.getUserId());
                    responseDTO.setProductId(requestDTO.getProductId());
                    responseDTO.setStatus(InventoryStatus.AVAILABLE);
                    return responseDTO;
                })
                .onErrorResume(throwable -> {
                    InventoryResponseDTO responseDTO = new InventoryResponseDTO();
                    responseDTO.setOrderId(requestDTO.getOrderId());
                    responseDTO.setUserId(requestDTO.getUserId());
                    responseDTO.setProductId(requestDTO.getProductId());
                    responseDTO.setStatus(InventoryStatus.UNAVAILABLE);
                    return Mono.just(responseDTO);
                });
    }

    public Mono<Void> addInventory(final InventoryRequestDTO requestDTO) {
        return inventoryRepository.findById(requestDTO.getProductId())
                .flatMap(inventory -> {
                    inventory.setQuantity(inventory.getQuantity() + 1);
                    log.info("Add Inventory, Remaining: " + inventory);
                    return inventoryRepository.save(inventory);
                })
                .then();
    }
}

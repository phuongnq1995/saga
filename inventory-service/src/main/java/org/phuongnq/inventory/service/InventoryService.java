package org.phuongnq.inventory.service;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.InventoryRequestDTO;
import org.phuongnq.dto.response.InventoryResponseDTO;
import org.phuongnq.enums.InventoryStatus;
import org.phuongnq.inventory.entity.Inventory;
import org.phuongnq.inventory.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;

    @Transactional
    public InventoryResponseDTO deductInventory(final InventoryRequestDTO requestDTO) {
        var inventory = inventoryRepository.findById(requestDTO.getProductId()).orElseThrow();
        var quantity = inventory.getQuantity();

        InventoryResponseDTO responseDTO = new InventoryResponseDTO();
        responseDTO.setOrderId(requestDTO.getOrderId());
        responseDTO.setUserId(requestDTO.getUserId());
        responseDTO.setProductId(requestDTO.getProductId());
        responseDTO.setStatus(InventoryStatus.UNAVAILABLE);
        if (quantity > 0) {
            responseDTO.setStatus(InventoryStatus.AVAILABLE);
            inventory.setQuantity(inventory.getQuantity() - 1);
            log.info("Deduct Inventory, Remaining: " + inventory);
            inventoryRepository.save(inventory);
        }
        log.info("Remaining: " + inventory);
        return responseDTO;
    }

    @Transactional
    public void addInventory(final InventoryRequestDTO requestDTO) {
        var inventory = inventoryRepository.findById(requestDTO.getProductId()).orElseThrow();
        inventory.setQuantity(inventory.getQuantity() + 1);
        log.info("Add Inventory, Remaining: " + inventory);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void cleanData() {
        inventoryRepository.deleteAll();
        inventoryRepository.saveAll(List.of(
                new Inventory(1, 5),
                new Inventory(2, 5),
                new Inventory(3, 5)
        ));
    }
}

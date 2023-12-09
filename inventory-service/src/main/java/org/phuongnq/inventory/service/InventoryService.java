package org.phuongnq.inventory.service;

import jakarta.annotation.PostConstruct;
import org.phuongnq.dto.request.InventoryRequestDTO;
import org.phuongnq.dto.response.InventoryResponseDTO;
import org.phuongnq.enums.InventoryStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {

    private Map<Integer, Integer> productInventoryMap;

    @PostConstruct
    private void init() {
        this.productInventoryMap = new HashMap<>();
        this.productInventoryMap.put(1, 5);
        this.productInventoryMap.put(2, 5);
        this.productInventoryMap.put(3, 5);
    }

    public InventoryResponseDTO deductInventory(final InventoryRequestDTO requestDTO) {
        int quantity = productInventoryMap.getOrDefault(requestDTO.getProductId(), 0);
        InventoryResponseDTO responseDTO = new InventoryResponseDTO();
        responseDTO.setOrderId(requestDTO.getOrderId());
        responseDTO.setUserId(requestDTO.getUserId());
        responseDTO.setProductId(requestDTO.getProductId());
        responseDTO.setStatus(InventoryStatus.UNAVAILABLE);
        if (quantity > 0) {
            responseDTO.setStatus(InventoryStatus.AVAILABLE);
            this.productInventoryMap.put(requestDTO.getProductId(), quantity - 1);
        }
        System.out.println("Remaining: " + productInventoryMap.toString());
        return responseDTO;
    }

    public void addInventory(final InventoryRequestDTO requestDTO) {
        this.productInventoryMap
                .computeIfPresent(requestDTO.getProductId(), (k, v) -> v + 1);
        System.out.println("Remaining: " + productInventoryMap.toString());
    }

}

package org.phuongnq.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryInitialize implements ApplicationRunner {
    private final InventoryService inventoryService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        inventoryService.cleanData();
    }
}

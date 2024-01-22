package org.phuongnq.inventory.repository;

import org.phuongnq.inventory.entity.Inventory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends ReactiveCrudRepository<Inventory, Integer> {
}

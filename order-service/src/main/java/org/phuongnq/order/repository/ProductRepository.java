package org.phuongnq.order.repository;

import org.phuongnq.order.entity.Product;
import org.phuongnq.order.entity.PurchaseOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
}

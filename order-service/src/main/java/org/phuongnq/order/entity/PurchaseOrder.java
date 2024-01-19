package org.phuongnq.order.entity;

import lombok.Data;
import lombok.ToString;
import org.phuongnq.enums.OrderStatus;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
public class PurchaseOrder {

    @Id
    private UUID id;
    private Integer userId;
    private Integer productId;
    private Double price;
    private OrderStatus status;

}
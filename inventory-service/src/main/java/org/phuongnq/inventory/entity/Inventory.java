package org.phuongnq.inventory.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Inventory {

    @Id
    private Integer productId;
    private Integer quantity;
}
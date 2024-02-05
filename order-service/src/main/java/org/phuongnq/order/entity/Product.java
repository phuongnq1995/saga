package org.phuongnq.order.entity;

import lombok.Data;
import org.phuongnq.enums.OrderStatus;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
public class Product {

    @Id
    private Integer id;
    private String name;
    private Double price;
}
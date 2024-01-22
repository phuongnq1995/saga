package org.phuongnq.payment.entity;

import lombok.Data;
import org.phuongnq.enums.OrderStatus;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
public class Payment {

    @Id
    private Integer userId;
    private Double balance;
}
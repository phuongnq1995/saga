package org.phuongnq.payment.controller;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.PaymentRequestDTO;
import org.phuongnq.dto.response.PaymentResponseDTO;
import org.phuongnq.payment.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/debit")
    public Mono<PaymentResponseDTO> debit(@RequestBody PaymentRequestDTO requestDTO) {
        return service.debit(requestDTO);
    }

    @PostMapping("/credit")
    public Mono<Void> credit(@RequestBody PaymentRequestDTO requestDTO) {
        return service.credit(requestDTO);
    }
}

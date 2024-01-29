package org.phuongnq.payment.controller;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.PaymentRequestDTO;
import org.phuongnq.dto.response.PaymentResponseDTO;
import org.phuongnq.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/debit")
    public ResponseEntity<PaymentResponseDTO> debit(@RequestBody PaymentRequestDTO requestDTO) {
        return ResponseEntity.ok(service.debit(requestDTO));
    }

    @PostMapping("/credit")
    public ResponseEntity<Void> credit(@RequestBody PaymentRequestDTO requestDTO) {
        service.credit(requestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> cleanData() {
        service.cleanData();
        return ResponseEntity.ok().build();
    }
}

package org.phuongnq.payment.service;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.request.PaymentRequestDTO;
import org.phuongnq.dto.response.PaymentResponseDTO;
import org.phuongnq.enums.PaymentStatus;
import org.phuongnq.payment.entity.Payment;
import org.phuongnq.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponseDTO debit(final PaymentRequestDTO requestDTO) {
        log.info("Debit request {}", requestDTO);
        var payment = paymentRepository.findById(requestDTO.getUserId()).orElseThrow();
        var balance = payment.getBalance();
        var responseDTO = new PaymentResponseDTO();
        responseDTO.setAmount(requestDTO.getAmount());
        responseDTO.setUserId(requestDTO.getUserId());
        responseDTO.setOrderId(requestDTO.getOrderId());
        responseDTO.setStatus(PaymentStatus.PAYMENT_REJECTED);
        if (balance >= requestDTO.getAmount()) {
            responseDTO.setStatus(PaymentStatus.PAYMENT_APPROVED);
            payment.setBalance(payment.getBalance() - requestDTO.getAmount());
            log.info("Debit, Balance of user {}", payment);
            paymentRepository.save(payment);
        }
        log.info("Remaining: " + payment);
        return responseDTO;
    }

    @Transactional
    public void credit(final PaymentRequestDTO requestDTO) {
        log.info("Credit request {}", requestDTO);
        var payment = paymentRepository.findById(requestDTO.getUserId()).orElseThrow();
        payment.setBalance(payment.getBalance() + requestDTO.getAmount());
        log.info("Credit, Balance of user {}", payment);
        paymentRepository.save(payment);
    }

    @Transactional
    public void cleanData() {
        paymentRepository.deleteAll();
        paymentRepository.saveAll(List.of(
                new Payment(1, 1000d),
                new Payment(2, 1000d),
                new Payment(3, 1000d)
        ));
    }
}

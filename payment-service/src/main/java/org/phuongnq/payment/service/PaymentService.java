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
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;

    public Mono<PaymentResponseDTO> debit(final PaymentRequestDTO requestDTO) {
        log.info("Debit request {}", requestDTO);
        return paymentRepository.findById(requestDTO.getUserId())
                .flatMap(payment -> {
                    if (payment.getBalance() >= requestDTO.getAmount()) {
                        payment.setBalance(payment.getBalance() - requestDTO.getAmount());
                        log.info("Debit, Balance of user {}", payment);
                        return paymentRepository.save(payment);
                    }
                    throw new IllegalArgumentException();
                })
                .map(payment -> {
                    PaymentResponseDTO responseDTO = new PaymentResponseDTO();
                    responseDTO.setUserId(requestDTO.getUserId());
                    responseDTO.setOrderId(requestDTO.getOrderId());
                    responseDTO.setAmount(payment.getBalance());
                    responseDTO.setStatus(PaymentStatus.PAYMENT_APPROVED);
                    return responseDTO;
                })
                .onErrorResume(throwable -> {
                    PaymentResponseDTO responseDTO = new PaymentResponseDTO();
                    responseDTO.setAmount(requestDTO.getAmount());
                    responseDTO.setUserId(requestDTO.getUserId());
                    responseDTO.setOrderId(requestDTO.getOrderId());
                    responseDTO.setStatus(PaymentStatus.PAYMENT_REJECTED);
                    return Mono.just(responseDTO);
                });
    }

    public Mono<Void> credit(final PaymentRequestDTO requestDTO) {
        log.info("Credit request {}", requestDTO);
        return paymentRepository.findById(requestDTO.getUserId())
                .flatMap(payment -> {
                    payment.setBalance(payment.getBalance() + requestDTO.getAmount());
                    log.info("Credit, Balance of user {}", payment);
                    return paymentRepository.save(payment);
                })
                .then();
    }
}

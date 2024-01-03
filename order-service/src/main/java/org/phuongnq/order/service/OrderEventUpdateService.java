package org.phuongnq.order.service;

import lombok.RequiredArgsConstructor;
import org.phuongnq.dto.response.OrchestratorResponseDTO;
import org.phuongnq.order.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderEventUpdateService {

    private final PurchaseOrderRepository repository;

    public Mono<Void> updateOrder(final OrchestratorResponseDTO responseDTO) {
        return repository.findById(responseDTO.getOrderId())
                .doOnNext(p -> p.setStatus(responseDTO.getStatus()))
                .flatMap(repository::save)
                .then();
    }
}

package org.phuongnq.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentInitialize implements ApplicationRunner {
    private final PaymentService service;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        service.cleanData();
    }
}

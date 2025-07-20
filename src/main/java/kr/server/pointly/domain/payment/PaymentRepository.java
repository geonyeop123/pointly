package kr.server.pointly.domain.payment;

import java.util.Optional;

public interface PaymentRepository {

    public Payment save(Payment payment);
    public Optional<Payment> findById(Long id);
}
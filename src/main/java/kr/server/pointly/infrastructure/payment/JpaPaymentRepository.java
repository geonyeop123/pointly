package kr.server.pointly.infrastructure.payment;

import kr.server.pointly.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends JpaRepository<Payment, Long> {

}

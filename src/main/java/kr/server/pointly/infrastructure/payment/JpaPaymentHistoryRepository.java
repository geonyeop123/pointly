package kr.server.pointly.infrastructure.payment;

import kr.server.pointly.domain.payment.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
}

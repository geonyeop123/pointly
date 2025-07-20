package kr.server.pointly.infrastructure.payment;

import kr.server.pointly.domain.payment.PaymentHistory;
import kr.server.pointly.domain.payment.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentHistoryRepositoryImpl implements PaymentHistoryRepository {

    private final JpaPaymentHistoryRepository jpaPaymentHistoryRepository;

    @Override
    public void save(PaymentHistory paymentHistory) {
        jpaPaymentHistoryRepository.save(paymentHistory);
    }
}

package kr.server.pointly.domain.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    public Payment create(PaymentCommand.Create command){
        Payment payment = Payment.create(command.user().getId(), command.amount(), command.PGType());

        return paymentRepository.save(payment);
    }

    public Payment complete(PaymentCommand.Complete command){
        Long id = command.paymentId();
        Payment payment = getPaymentById(id);

        payment.complete(command.amount(), command.PGType(), command.orderId());

        PaymentHistory history = PaymentHistory.paid(payment, LocalDateTime.now());
        paymentHistoryRepository.save(history);

        return payment;
    }

    public Payment cancel(PaymentCommand.Cancel command) {
        Long id = command.paymentId();
        Payment payment = getPaymentById(id);

        payment.cancel();

        return payment;
    }

    public Payment fail(PaymentCommand.Fail command){
        Long id = command.paymentId();
        Payment payment = getPaymentById(id);

        payment.fail();

        return payment;
    }

    private Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당되는 결제가 없습니다."));
    }
}

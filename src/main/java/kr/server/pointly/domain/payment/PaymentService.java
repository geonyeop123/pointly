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
        Payment payment = Payment.create(command.user().getId(), command.amount(), command.paymentType());
        return paymentRepository.save(payment);
    }

    public Payment complete(PaymentCommand.Complete command){
        Payment payment = paymentRepository.findById(command.paymentId())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 결제가 없습니다."));

        payment.complete(command.amount(), command.paymentType());

        PaymentHistory history = PaymentHistory.paid(payment, command.paymentToken(), LocalDateTime.now());
        paymentHistoryRepository.save(history);

        return payment;
    }

    public Payment cancel(PaymentCommand.Cancel command) {
        Payment payment = paymentRepository.findById(command.paymentId())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 결제가 없습니다."));

        payment.cancel();

        return payment;
    }

    public Payment fail(PaymentCommand.Fail command){
        Payment payment = paymentRepository.findById(command.paymentId())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 결제가 없습니다."));

        payment.fail();

        return payment;
    }
}

package kr.server.pointly.support.exception;

import kr.server.pointly.domain.payment.PaymentStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidPaymentStatusException extends BusinessException {
    public InvalidPaymentStatusException(PaymentStatus before, PaymentStatus after, Long paymentId) {
        super(String.format("결제 상태가 %s일 때는 %s(으)로 변경할 수 없습니다.", after, before));
        log.error("InvalidPaymentStatusException, paymentId : {}", paymentId);
    }
}

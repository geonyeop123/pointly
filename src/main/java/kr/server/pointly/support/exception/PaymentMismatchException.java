package kr.server.pointly.support.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentMismatchException extends BusinessException {
    public PaymentMismatchException(Long paymentId){
        super("결제 정보가 일치하지 않습니다.");
        log.error("PaymentMismatchException, paymentId : {}", paymentId);
    }
}

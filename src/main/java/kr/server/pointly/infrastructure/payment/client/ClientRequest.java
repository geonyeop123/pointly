package kr.server.pointly.infrastructure.payment.client;

import kr.server.pointly.domain.payment.PaymentCommand;

public record ClientRequest(

) {
    public record TossConfirm(
        String paymentKey,
        String orderId,
        Long amount
    ) {
        public static TossConfirm from(PaymentCommand .Complete command) {
            return new TossConfirm(command.paymentToken(), command.orderId(), command.amount());
        }
    }
}

package kr.server.pointly.domain.payment.client;

public record ClientResult(
        String paymentKey,
        String orderId,
        String message,
        String code
) {

}

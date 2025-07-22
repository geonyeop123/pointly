package kr.server.pointly.infrastructure.payment.client;

import kr.server.pointly.domain.payment.client.ClientResult;

public record ClientResponse (

){
    public record TossConfirm(
            String paymentKey,
            String orderId,
            String method,
            Long totalAmount
    ) {
        public ClientResult toDomain(){
            return new ClientResult(paymentKey, orderId, null, null);
        }
    }

}

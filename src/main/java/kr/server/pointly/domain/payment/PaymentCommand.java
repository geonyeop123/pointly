package kr.server.pointly.domain.payment;

import kr.server.pointly.domain.user.User;

public record PaymentCommand(
) {

    public record Create(
            User user,
            Long amount,
            PaymentType paymentType
    ){

    }
    public record Complete(
            User user,
            Long paymentId,
            Long amount,
            PaymentType paymentType,
            String paymentToken
    ) {

    }

    public record Cancel(
            User user,
            Long paymentId
    ) {

    }

    public record Fail(
            User user,
            Long paymentId
    ) {
    }
}

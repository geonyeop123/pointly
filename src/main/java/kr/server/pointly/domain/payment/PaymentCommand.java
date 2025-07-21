package kr.server.pointly.domain.payment;

import kr.server.pointly.domain.user.User;

public record PaymentCommand(
) {

    public record Create(
            User user,
            Long amount,
            PGType PGType
    ){

    }
    public record Complete(
            User user,
            Long paymentId,
            Long amount,
            PGType PGType,
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

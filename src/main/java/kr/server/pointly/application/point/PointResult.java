package kr.server.pointly.application.point;

import kr.server.pointly.domain.payment.Payment;
import kr.server.pointly.domain.user.User;

import java.time.LocalDateTime;

public record PointResult(

) {
    public record RequestCharge(
            Long userId,
            Long paymentId,
            Long amount,
            String pgType,
            LocalDateTime paidRequestAt
    ) {
        public static RequestCharge from(User user, Payment payment){
            return new RequestCharge(user.getId(), payment.getId(),
                    payment.getAmount(), payment.getType().name(), payment.getCreatedAt());
        }
    }

    public record CancelCharge(
        Long userId,
        Long paymentId,
        LocalDateTime canceledAt
    ){
        public static CancelCharge from(User user, Payment payment) {
            return new CancelCharge(user.getId(), payment.getId(), payment.getModifiedAt());
        }
    }
}

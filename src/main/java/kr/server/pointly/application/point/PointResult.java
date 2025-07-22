package kr.server.pointly.application.point;

import kr.server.pointly.domain.payment.Payment;
import kr.server.pointly.domain.user.User;

import java.time.LocalDateTime;

public record PointResult(

) {
    public record ChargeRequest(
            Long userId,
            Long paymentId,
            Long amount,
            String pgType,
            LocalDateTime paidRequestAt
    ) {
        public static PointResult.ChargeRequest from(User user, Payment payment){
            return new PointResult.ChargeRequest(user.getId(), payment.getId(),
                    payment.getAmount(), payment.getType().name(), payment.getCreatedAt());
        }
    }
}

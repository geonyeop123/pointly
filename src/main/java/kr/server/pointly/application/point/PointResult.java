package kr.server.pointly.application.point;

import kr.server.pointly.domain.payment.Payment;
import kr.server.pointly.domain.point.Point;
import kr.server.pointly.domain.user.User;

import java.time.LocalDateTime;

public record PointResult(

) {
    public record RequestCharge(
            Long userId,
            Long paymentId,
            String orderId,
            Long amount,
            String pgType,
            LocalDateTime paidRequestAt
    ) {
        public static RequestCharge from(User user, Payment payment){
            return new RequestCharge(user.getId(), payment.getId(),
                    payment.getOrderId(), payment.getAmount(), payment.getType().name(), payment.getCreatedAt());
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

    public record CompleteCharge(
        Long userId,
        Long paymentId,
        Long pointBalance,
        Long paidAmount,
        String pgType,
        LocalDateTime paidAt
    ) {
        public static CompleteCharge from(User user, Payment payment, Point point){
            return new CompleteCharge(user.getId(), payment.getId(), point.getBalance(), payment.getAmount(), payment.getType().name(), payment.getModifiedAt());
        }
    }
}

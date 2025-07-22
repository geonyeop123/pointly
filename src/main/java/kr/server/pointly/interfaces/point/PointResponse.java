package kr.server.pointly.interfaces.point;

import kr.server.pointly.application.point.PointResult;

import java.time.LocalDateTime;

public record PointResponse(

) {
    public record RequestCharge(
        Long userId,
        Long paymentId,
        Long amount,
        String pgType,
        LocalDateTime paidRequestAt
    ) {
        public static RequestCharge from(PointResult.RequestCharge result){
            return new RequestCharge(result.userId(), result.paymentId(), result.amount(), result.pgType(), result.paidRequestAt());
        }
    }

    public record CompletedCharge(
            Long userId,
            Long balance,
            Long paymentId,
            Long paidAmount,
            String pgType,
            LocalDateTime paidAt
    ){

    }

    public record CanceledCharge(
            Long userId,
            Long paymentId,
            LocalDateTime canceledAt
    ) {
        public static CanceledCharge from(PointResult.CancelCharge result){
            return new CanceledCharge(result.userId(), result.paymentId(), result.canceledAt());
        }
    }
}

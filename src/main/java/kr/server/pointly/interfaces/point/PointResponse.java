package kr.server.pointly.interfaces.point;

import kr.server.pointly.application.point.PointResult;

import java.time.LocalDateTime;

public record PointResponse(

) {
    public record ChargeResponse(
        Long userId,
        Long paymentId,
        Long amount,
        String pgType,
        LocalDateTime paidRequestAt
    ) {
        public static PointResponse.ChargeResponse from(PointResult.ChargeRequest result){
            return new PointResponse.ChargeResponse(result.userId(), result.paymentId(), result.amount(), result.pgType(), result.paidRequestAt());
        }
    }

    public record ChargeCompletedResponse(
            Long userId,
            Long balance,
            Long paymentId,
            Long paidAmount,
            String pgType,
            LocalDateTime paidAt
    ){

    }

    public record ChargeCanceledResponse (
            Long userId,
            Long paymentId,
            LocalDateTime canceledAt
    ) {

    }
}

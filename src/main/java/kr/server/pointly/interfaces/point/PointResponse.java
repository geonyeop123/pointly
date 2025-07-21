package kr.server.pointly.interfaces.point;

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

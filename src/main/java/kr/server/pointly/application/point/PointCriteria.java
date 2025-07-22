package kr.server.pointly.application.point;

public record PointCriteria(

) {
    public record RequestCharge(
            Long userId,
            Long amount,
            String orderId,
            String paymentType
    ) {
        
    }
    public record CancelCharge(
            Long userId,
            Long paymentId
    ){

    }

    public record CompleteCharge(
            Long userId,
            Long paymentId,
            String orderId,
            Long amount,
            String pgType,
            String paymentToken
    ){

    }
}

package kr.server.pointly.application.point;

public record PointCriteria(

) {
    public record RequestCharge(
            Long userId,
            Long amount,
            String paymentType
    ) {
        
    }
    public record CancelCharge(
            Long userId,
            Long paymentId
    ){

    }
}

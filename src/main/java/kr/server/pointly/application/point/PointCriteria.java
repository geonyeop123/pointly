package kr.server.pointly.application.point;

public record PointCriteria(

) {
    public record ChargeRequest(
            Long userId,
            Long amount,
            String paymentType
    ) {
        
    }

}

package kr.server.pointly.interfaces.point;

import kr.server.pointly.application.point.PointCriteria;

public record PointRequest(

) {
    public record RequestCharge(
            Long amount,
            String pgType
    ){
        public PointCriteria.ChargeRequest toCriteria(Long userId){
            return new PointCriteria.ChargeRequest(userId, amount, pgType);
        }
    }

    public record CompleteCharge(
            Long paymentId,
            Long amount,
            String pgType,
            String paymentToken
    ){

    }

    public record CancelCharge(
            Long paymentId
    ){

    }
}

package kr.server.pointly.interfaces.point;

import kr.server.pointly.application.point.PointCriteria;

public record PointRequest(

) {
    public record RequestCharge(
            Long amount,
            String orderId,
            String pgType
    ){
        public PointCriteria.RequestCharge toCriteria(Long userId){
            return new PointCriteria.RequestCharge(userId, amount, orderId, pgType);
        }
    }

    public record CompleteCharge(
            Long paymentId,
            Long amount,
            String orderId,
            String pgType,
            String paymentToken
    ){
        public PointCriteria.CompleteCharge toCriteria(Long userId){
            return new PointCriteria.CompleteCharge(userId, paymentId, orderId, amount, pgType, paymentToken);
        }
    }

    public record CancelCharge(
            Long paymentId
    ){
        public PointCriteria.CancelCharge toCriteria(Long userId){
            return new PointCriteria.CancelCharge(userId, paymentId);
        }
    }
}

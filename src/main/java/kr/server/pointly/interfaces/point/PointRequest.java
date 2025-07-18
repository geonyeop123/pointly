package kr.server.pointly.interfaces.point;

public record PointRequest(

) {
    public record RequestCharge(
            Long amount,
            String paymentType
    ){
    }

    public record CompleteCharge(
            Long paymentId,
            Long amount,
            String paymentType,
            String paymentToken
    ){

    }

    public record CancelCharge(
            Long paymentId
    ){

    }
}

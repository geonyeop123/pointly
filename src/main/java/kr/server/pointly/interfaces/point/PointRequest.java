package kr.server.pointly.interfaces.point;

public record PointRequest(

) {
    public record RequestCharge(
            Long amount,
            String pgType
    ){
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

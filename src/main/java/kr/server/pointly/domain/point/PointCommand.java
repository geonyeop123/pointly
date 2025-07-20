package kr.server.pointly.domain.point;

public record PointCommand() {

    public record Charge(
            Long userId,
            Long amount
    ) {

    }
}
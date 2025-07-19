package kr.server.pointly.interfaces.common;

public record ErrorResponse(
        String code,
        String message
) {
}
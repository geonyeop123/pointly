package kr.server.pointly.support.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BusinessException extends RuntimeException {
    protected BusinessException(String message) {
        super(message);
    }
}
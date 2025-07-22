package kr.server.pointly.domain.payment.client;

import kr.server.pointly.domain.payment.PaymentCommand;

public interface PaymentClient {
    ClientResult completeRequest(PaymentCommand.Complete command);
}
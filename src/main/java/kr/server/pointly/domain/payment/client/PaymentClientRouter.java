package kr.server.pointly.domain.payment.client;

import kr.server.pointly.domain.payment.PGType;

public interface PaymentClientRouter {
    PaymentClient getClient(PGType pgType);
}

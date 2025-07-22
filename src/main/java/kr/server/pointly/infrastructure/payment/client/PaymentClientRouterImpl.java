package kr.server.pointly.infrastructure.payment.client;

import kr.server.pointly.domain.payment.PGType;
import kr.server.pointly.domain.payment.client.PaymentClient;
import kr.server.pointly.domain.payment.client.PaymentClientRouter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentClientRouterImpl implements PaymentClientRouter {

    private final Map<PGType, PaymentClient> clientMap;

    public PaymentClientRouterImpl(
            @Qualifier("TOSS") PaymentClient tossClient
    ) {
        this.clientMap = Map.of(
                PGType.TOSS, tossClient
        );
    }

    @Override
    public PaymentClient getClient(PGType type) {
        if (!clientMap.containsKey(type)) {
            throw new IllegalArgumentException("지원하지 않는 결제 제공자입니다: " + type);
        }
        return clientMap.get(type);
    }
}

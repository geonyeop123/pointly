package kr.server.pointly.infrastructure.payment.client;


import kr.server.pointly.domain.payment.client.ClientResult;
import kr.server.pointly.domain.payment.client.PaymentClient;
import kr.server.pointly.domain.payment.PaymentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Qualifier("TOSS")
@Component
@RequiredArgsConstructor
public class PaymentClientImpl implements PaymentClient {

    private final TossFeignClient tossFeignClient;

    @Value("${toss.secret-key}")
    private String tossSecretKey;


    @Override
    public ClientResult completeRequest(PaymentCommand.Complete command) {
        String authorization = AuthHeaderUtil.basicAuth(tossSecretKey + ":");
        ClientResponse.TossConfirm response = tossFeignClient.confirm(authorization, ClientRequest.TossConfirm.from(command));
        return response.toDomain();
    }
}

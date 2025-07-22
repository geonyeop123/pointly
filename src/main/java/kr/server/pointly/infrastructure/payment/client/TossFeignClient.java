package kr.server.pointly.infrastructure.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "tossClient", url = "${toss.api.url.confirm}")
public interface TossFeignClient {
    @PostMapping("/confirm")
    ClientResponse.TossConfirm confirm(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ClientRequest.TossConfirm request
    );
}
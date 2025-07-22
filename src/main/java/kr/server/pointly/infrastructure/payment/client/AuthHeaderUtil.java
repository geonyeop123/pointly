package kr.server.pointly.infrastructure.payment.client;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthHeaderUtil {
    public static String basicAuth(String secretKey) {
        return "Basic " + Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}

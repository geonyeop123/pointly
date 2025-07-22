package kr.server.pointly.interfaces.point;

import kr.server.pointly.application.point.PointResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PointResponseTest {
    @Nested
    class RequestCharge {
        @DisplayName("PointResult를 받아 ChargeResponse를 생성할 수 있다.")
        @Test
        void from() {
            // given
            PointResult.RequestCharge result = new PointResult.RequestCharge(1L, 1L, "orderId", 1000L, "TOSS", LocalDateTime.of(2025,7,22,0,0,0));

            // when
            PointResponse.RequestCharge response = PointResponse.RequestCharge.from(result);

            // then
            assertThat(response.userId()).isEqualTo(result.userId());
            assertThat(response.paymentId()).isEqualTo(result.paymentId());
            assertThat(response.amount()).isEqualTo(result.amount());
            assertThat(response.pgType()).isEqualTo(result.pgType());
            assertThat(response.paidRequestAt()).isEqualTo(result.paidRequestAt());
        }
    }

}
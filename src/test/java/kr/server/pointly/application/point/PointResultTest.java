package kr.server.pointly.application.point;

import kr.server.pointly.domain.payment.Payment;
import kr.server.pointly.domain.payment.PGType;
import kr.server.pointly.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointResultTest {

    @Nested
    class ChargeRequest {
        @DisplayName("user와 payment로 ChargeRequest를 생성할 수 있다.")
        @Test
        void createByUserAndPayment() {
            // given
            User user = User.create("이건엽");
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);

            // when
            PointResult.ChargeRequest result = PointResult.ChargeRequest.from(user, payment);

            // then
            assertThat(result.paymentId()).isEqualTo(payment.getId());
            assertThat(result.userId()).isEqualTo(user.getId());
            assertThat(result.amount()).isEqualTo(payment.getAmount());
            assertThat(result.pgType()).isEqualTo(payment.getType().name());
            assertThat(result.paidRequestAt()).isEqualTo(payment.getCreatedAt());
        }
    }

}
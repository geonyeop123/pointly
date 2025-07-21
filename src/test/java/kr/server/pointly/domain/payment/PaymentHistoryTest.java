package kr.server.pointly.domain.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentHistoryTest {

    @Nested
    class Paid{
        @DisplayName("paid()메서드로 결제 완료 이력을 생성할 수 있다.")
        @Test
        void success() {
            // given
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);
            payment.complete(1000L, PGType.TOSS);

            // when
            PaymentHistory history = PaymentHistory.paid(payment, "token", LocalDateTime.now());

            // then
            assertThat(history.getType()).isEqualTo(TransactionType.PAID);
        }

        @DisplayName("paid()메서드로 결제 완료 이력을 생성할 때 파라미터 Payment의 상태가 completed가 아니면 Exception이 발생한다.")
        @Test
        void failFromNotComplete() {
            // given
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);

            // when // then
            assertThatThrownBy(() -> PaymentHistory.paid(payment, "token", LocalDateTime.now()))
                    .isInstanceOf(IllegalStateException.class);
        }

    }


}
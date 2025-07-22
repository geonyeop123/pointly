package kr.server.pointly.domain.payment;

import kr.server.pointly.support.exception.InvalidPaymentStatusException;
import kr.server.pointly.support.exception.PaymentMismatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    @DisplayName("Payment를 생성하면 최초 상태는 PENDING이며, orderId가 생성된다.")
    @Test
    void create() {
        // given // when
        Payment payment = Payment.create(1L, 1000L, PGType.TOSS);

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(payment.getOrderId()).isNotNull();
    }

    @Nested
    class Complete {

        @DisplayName("PENDING 상태에서 complete()를 호출하면 COMPLETED로 변경된다.")
        @Test
        void success() {
            // given
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);

            // when
            payment.complete(1000L, PGType.TOSS, payment.getOrderId());

            // then
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        }

        @DisplayName("CANCELED 상태에서 complete()를 호출하면 InvalidPaymentStatusException이 발생한다.")
        @Test
        void failFromCanceled() {
            // given
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);
            payment.cancel();
            // when // then
            assertThatThrownBy(() -> payment.complete(1000L, PGType.TOSS, payment.getOrderId()))
                    .isInstanceOf(InvalidPaymentStatusException.class);
        }

        @DisplayName("COMPLETED 상태에서 complete()를 호출하면 InvalidPaymentStatusException이 발생한다.")
        @Test
        void failFromCompleted() {
            // given
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);
            payment.complete(1000L, PGType.TOSS, payment.getOrderId());

            // when // then
            assertThatThrownBy(() -> payment.complete(1000L, PGType.TOSS, payment.getOrderId()))
                    .isInstanceOf(InvalidPaymentStatusException.class);
        }

        @DisplayName("기존의 결제 금액과 결제 금액이 맞지 않는 경우 PaymentMismatchException이  발생한다.")
        @Test
        void failMismatchAmount() {
            // given
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);
            // when // then
            assertThatThrownBy(() -> payment.complete(2000L, PGType.TOSS, payment.getOrderId()))
                    .isInstanceOf(PaymentMismatchException.class);
        }

        @DisplayName("기존의 결제 유형과 실제 결제 유형이 맞지 않는 경우 PaymentMismatchException이  발생한다.")
        @Test
        void failMismatchType() {
            // given
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);
            // when // then
            assertThatThrownBy(() -> payment.complete(1000L, null, payment.getOrderId()))
                    .isInstanceOf(PaymentMismatchException.class);
        }

        @DisplayName("기존의 orderId와 받은 orderId가 맞지 않는 경우 PaymentMismatchException이  발생한다.")
        @Test
        void failMismatchOrderId() {
            // given
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);
            // when // then
            assertThatThrownBy(() -> payment.complete(1000L, null, payment.getOrderId()))
                    .isInstanceOf(PaymentMismatchException.class);
        }
    }

    @Nested
    class Cancel {

        @DisplayName("PENDING 상태에서 cancel()을 호출하면 CANCELED 로 변경된다.")
        @Test
        void success() {
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);

            payment.cancel();

            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
        }

        @DisplayName("COMPLETED 상태에서 cancel()을 호출하면 InvalidPaymentStatusException이 발생한다.")
        @Test
        void failFromCompleted() {
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);
            payment.complete(1000L, PGType.TOSS, payment.getOrderId());

            assertThatThrownBy(payment::cancel)
                    .isInstanceOf(InvalidPaymentStatusException.class);
        }

        @DisplayName("CANCELED 상태에서 cancel()을 호출하면 InvalidPaymentStatusException이 발생한다.")
        @Test
        void failFromCanceled() {
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);
            payment.cancel();

            assertThatThrownBy(payment::cancel)
                    .isInstanceOf(InvalidPaymentStatusException.class);
        }
    }

    @Nested
    class FAIL {

        @DisplayName("COMPLETED 상태에서 fail()을 호출하면 FAILED 로 변경된다.")
        @Test
        void success() {
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);
            payment.complete(1000L, PGType.TOSS, payment.getOrderId());

            payment.fail();

            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
        }

        @DisplayName("PENDING 상태에서 fail()을 호출하면 InvalidPaymentStatusException이 발생한다.")
        @Test
        void failFromCompleted() {
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);

            assertThatThrownBy(payment::fail)
                    .isInstanceOf(InvalidPaymentStatusException.class);
        }

        @DisplayName("CANCELED 상태에서 fail()을 호출하면 InvalidPaymentStatusException이 발생한다.")
        @Test
        void failFromCanceled() {
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);
            payment.cancel();

            assertThatThrownBy(payment::fail)
                    .isInstanceOf(InvalidPaymentStatusException.class);
        }
    }
}
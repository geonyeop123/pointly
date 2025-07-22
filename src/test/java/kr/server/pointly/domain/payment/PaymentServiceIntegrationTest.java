package kr.server.pointly.domain.payment;

import kr.server.pointly.domain.user.User;
import kr.server.pointly.infrastructure.payment.JpaPaymentHistoryRepository;
import kr.server.pointly.infrastructure.payment.JpaPaymentRepository;
import kr.server.pointly.infrastructure.user.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class PaymentServiceIntegrationTest {
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private JpaPaymentRepository jpaPaymentRepository;
    
    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaPaymentHistoryRepository jpaPaymentHistoryRepository;

    @DisplayName("command를 받아 payment, history를 생성하여 저장 후 payment를 반환한다.")
    @Test
    void create() {
        // given
        User user = jpaUserRepository.save(User.create("이건엽"));
        Long amount = 1000L;
        PaymentCommand.Create command = new PaymentCommand.Create(user, amount, "orderId", PGType.TOSS);

        // when
        Payment payment = paymentService.create(command);

        // then
        assertThat(payment).isNotNull();
        assertThat(jpaPaymentRepository.findById(payment.getId())).isPresent();
    }

    @Nested
    class Complete{

        @DisplayName("command를 받아 Payment를 complete로 변경하며, 이력과 함께 저장한다.")
        @Test
        void success() {
            // given
            User user = jpaUserRepository.save(User.create("이건엽"));
            Long userId = user.getId();
            Long amount = 1000L;
            Payment payment = jpaPaymentRepository.save(Payment.create(userId, 1000L, PGType.TOSS));
            PaymentCommand.Complete command = new PaymentCommand.Complete(user, payment.getId(), amount, payment.getOrderId(), PGType.TOSS, "paymentToken");

            // when
            Payment completedPayment = paymentService.complete(command);

            // then
            assertThat(completedPayment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
            List<PaymentHistory> histories = jpaPaymentHistoryRepository.findAll();
            assertThat(histories.size()).isEqualTo(1);
            PaymentHistory history = histories.getFirst();
            assertThat(history.getPaymentId()).isEqualTo(payment.getId());
            assertThat(history.getType()).isEqualTo(TransactionType.PAID);
        }

        @DisplayName("command에 해당하는 Payment가 조회되지 않은 경우 IllegalArgumentException가 발생한다.")
        @Test
        void failNotFoundPayment() {
            // given
            User user = jpaUserRepository.save(User.create("이건엽"));
            Long amount = 1000L;
            Long paymentId = 1L;
            PaymentCommand.Complete command = new PaymentCommand.Complete(user, paymentId, amount, "orderId", PGType.TOSS, "paymentToken");

            // when // then
            assertThatThrownBy(() -> paymentService.complete(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class Cancel{

        @DisplayName("command를 받아 Payment를 canceled로 변경한다.")
        @Test
        void success() {
            // given
            User user = jpaUserRepository.save(User.create("이건엽"));
            Long userId = user.getId();
            Payment payment = jpaPaymentRepository.save(Payment.create(userId, 1000L, PGType.TOSS));
            PaymentCommand.Cancel command = new PaymentCommand.Cancel(user, payment.getId());

            // when
            Payment completedPayment = paymentService.cancel(command);

            // then
            assertThat(completedPayment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
        }

        @DisplayName("command에 해당하는 Payment가 조회되지 않은 경우 IllegalArgumentException가 발생한다.")
        @Test
        void failNotFoundPayment() {
            // given
            User user = jpaUserRepository.save(User.create("이건엽"));
            Long paymentId = 1L;
            PaymentCommand.Cancel command = new PaymentCommand.Cancel(user, paymentId);

            // when // then
            assertThatThrownBy(() -> paymentService.cancel(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class Fail{

        @DisplayName("command를 받아 Payment를 failed 변경한다.")
        @Test
        void success() {
            // given
            User user = jpaUserRepository.save(User.create("이건엽"));
            Long userId = user.getId();
            Payment payment = Payment.create(userId, 1000L, PGType.TOSS);
            payment.complete(1000L, PGType.TOSS, payment.getOrderId());
            jpaPaymentRepository.save(payment);
            PaymentCommand.Fail command = new PaymentCommand.Fail(user, payment.getId());

            // when
            Payment completedPayment = paymentService.fail(command);

            // then
            assertThat(completedPayment.getStatus()).isEqualTo(PaymentStatus.FAILED);
        }

        @DisplayName("command에 해당하는 Payment가 조회되지 않은 경우 IllegalArgumentException가 발생한다.")
        @Test
        void failNotFoundPayment() {
            // given
            User user = jpaUserRepository.save(User.create("이건엽"));
            Long paymentId = 1L;
            PaymentCommand.Fail command = new PaymentCommand.Fail(user, paymentId);

            // when // then
            assertThatThrownBy(() -> paymentService.fail(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
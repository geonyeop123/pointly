package kr.server.pointly.domain.payment;

import kr.server.pointly.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;

    @InjectMocks
    private PaymentService paymentService;

    @DisplayName("결제 생성 요청 시 command를 받아 해당하는 payment를 생성, 저장 후 반환한다.")
    @Test
    void create() {
        // given
        PaymentCommand.Create command = new PaymentCommand.Create(User.create("이건엽"), 1000L, "orderId", PGType.TOSS);
        Payment payment = Payment.create(command.user().getId(), command.amount(), command.PGType());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // when
        Payment savedPayment = paymentService.create(command);

        // then
        assertThat(savedPayment).isNotNull();
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Nested
    class Complete{
        @DisplayName("결제 완료 요청 시 payment의 값을 갱신하고 paymentHistory를 저장한다.")
        @Test
        void success() {
            // given
            Payment payment = Payment.create(1L, 1000L, PGType.TOSS);
            PaymentCommand.Complete command =
                    new PaymentCommand.Complete(User.create("이건엽"), payment.getId(), payment.getAmount(), payment.getOrderId(), payment.getType(), "paymentToken");
            when(paymentRepository.findById(command.paymentId())).thenReturn(java.util.Optional.of(payment));
            doNothing().when(paymentHistoryRepository).save(any(PaymentHistory.class));
            // when
            Payment completedPayment = paymentService.complete(command);

            // then
            assertThat(completedPayment).isNotNull();
            verify(paymentRepository, times(1)).findById(command.paymentId());
            verify(paymentHistoryRepository, times(1)).save(any(PaymentHistory.class));
        }

        @DisplayName("결제 완료 요청 시 paymentId에 해당하는 Payment를 찾지 못한 경우 IllegalArgumentException이 발생한다.")
        @Test
        void failNotFoundPayment() {
            // given
            PaymentCommand.Complete command =
                    new PaymentCommand.Complete(User.create("이건엽"), 1L, 1000L, "orderId", PGType.TOSS, "paymentToken");

            // when // then
            assertThatThrownBy(() -> paymentService.complete(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당되는 결제가 없습니다.");
            verify(paymentRepository, times(1)).findById(command.paymentId());
            verify(paymentHistoryRepository, never()).save(any(PaymentHistory.class));
        }
    }

    @Nested
    class Cancel{
        @DisplayName("결제 취소 요청 시 payment의 값을 갱신한다.")
        @Test
        void success() {
            // given
            PaymentCommand.Cancel command =
                    new PaymentCommand.Cancel(User.create("이건엽"), 1L);
            when(paymentRepository.findById(command.paymentId())).thenReturn(java.util.Optional.of(Payment.create(command.paymentId(), 1000L, PGType.TOSS)));
            // when
            Payment canceledPayment = paymentService.cancel(command);

            // then
            assertThat(canceledPayment).isNotNull();
            verify(paymentRepository, times(1)).findById(command.paymentId());
        }

        @DisplayName("결제 취소 요청 시 paymentId에 해당하는 Payment를 찾지 못한 경우 IllegalArgumentException이 발생한다.")
        @Test
        void failNotFoundPayment() {
            // given
            PaymentCommand.Cancel command =
                    new PaymentCommand.Cancel(User.create("이건엽"), 1L);

            // when // then
            assertThatThrownBy(() -> paymentService.cancel(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당되는 결제가 없습니다.");
            verify(paymentRepository, times(1)).findById(command.paymentId());
        }
    }

    @Nested
    class Fail{
        @DisplayName("결제 실패 요청 시 payment의 값을 갱신한다.")
        @Test
        void success() {
            // given
            PaymentCommand.Fail command =
                    new PaymentCommand.Fail(User.create("이건엽"), 1L);
            Payment payment = Payment.create(command.paymentId(), 1000L, PGType.TOSS);
            payment.complete(1000L, PGType.TOSS, payment.getOrderId());
            when(paymentRepository.findById(command.paymentId())).thenReturn(java.util.Optional.of(payment));
            // when
            Payment canceledPayment = paymentService.fail(command);

            // then
            assertThat(canceledPayment).isNotNull();
            verify(paymentRepository, times(1)).findById(command.paymentId());
        }

        @DisplayName("결제 실패 요청 시 paymentId에 해당하는 Payment를 찾지 못한 경우 IllegalArgumentException이 발생한다.")
        @Test
        void failNotFoundPayment() {
            // given
            PaymentCommand.Fail command =
                    new PaymentCommand.Fail(User.create("이건엽"), 1L);

            // when // then
            assertThatThrownBy(() -> paymentService.fail(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당되는 결제가 없습니다.");
            verify(paymentRepository, times(1)).findById(command.paymentId());
        }
    }

}
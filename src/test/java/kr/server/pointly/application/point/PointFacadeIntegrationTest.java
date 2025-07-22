package kr.server.pointly.application.point;

import kr.server.pointly.domain.payment.PGType;
import kr.server.pointly.domain.payment.Payment;
import kr.server.pointly.domain.payment.PaymentCommand;
import kr.server.pointly.domain.payment.PaymentStatus;
import kr.server.pointly.domain.payment.client.ClientResult;
import kr.server.pointly.domain.payment.client.PaymentClient;
import kr.server.pointly.domain.point.Point;
import kr.server.pointly.domain.user.User;
import kr.server.pointly.infrastructure.payment.JpaPaymentRepository;
import kr.server.pointly.infrastructure.point.JpaPointRepository;
import kr.server.pointly.infrastructure.user.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class PointFacadeIntegrationTest {

    @Autowired
    private PointFacade pointFacade;

    @MockitoBean
    private PaymentClient paymentClient;

    @Autowired
    private JpaPointRepository jpaPointRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaPaymentRepository jpaPaymentRepository;

    @DisplayName("포인트 충전을 요청할 수 있다.")
    @Test
    void requestCharge() {
        // given
        User user = jpaUserRepository.save(User.create("이건엽"));
        jpaPointRepository.save(Point.create(user.getId(), 1000L));
        PointCriteria.RequestCharge criteria = new PointCriteria.RequestCharge(user.getId(), 1000L, "orderId", "TOSS");

        // when
        PointResult.RequestCharge result = pointFacade.requestCharge(criteria);
        // then
        assertThat(result.amount()).isEqualTo(1000L);
        assertThat(result.pgType()).isEqualTo("TOSS");
    }

    @Nested
    class CompleteCharge{
        @DisplayName("포인트 충전을 완료할 수 있다.")
        @Test
        void success() {
            // given
            User user = jpaUserRepository.save(User.create("이건엽"));
            jpaPointRepository.save(Point.create(user.getId(), 1000L));
            Payment payment = jpaPaymentRepository.save(Payment.create(user.getId(), 1000L, PGType.TOSS));
            String orderId = payment.getOrderId();
            String token = "token";
            PointCriteria.CompleteCharge criteria = new PointCriteria.CompleteCharge(user.getId(), payment.getId(), orderId, payment.getAmount(), payment.getType().toString(), token);
            when(paymentClient.completeRequest(new PaymentCommand.Complete(user, payment.getId(), payment.getAmount(), orderId, PGType.TOSS, token)))
                    .thenReturn(new ClientResult(token, orderId, null, null));

            // when
            PointResult.CompleteCharge result = pointFacade.completeCharge(criteria);

            // then
            assertThat(result.paymentId()).isEqualTo(payment.getId());
            assertThat(result.paidAmount()).isEqualTo(payment.getAmount());
            assertThat(result.pointBalance()).isEqualTo(2000L);
            assertThat(result.pgType()).isEqualTo(payment.getType().name());
        }

        @DisplayName("포인트 충전 시 Client에서 오류가 발생했을 때 payment의 상태가 FAIL 로 변경된다.")
        @Test
        void failByClientException() {
            // given
            User user = jpaUserRepository.save(User.create("이건엽"));
            jpaPointRepository.save(Point.create(user.getId(), 1000L));
            Payment payment = jpaPaymentRepository.save(Payment.create(user.getId(), 1000L, PGType.TOSS));
            String orderId = payment.getOrderId();
            String token = "token";
            PointCriteria.CompleteCharge criteria = new PointCriteria.CompleteCharge(user.getId(), payment.getId(), orderId, payment.getAmount(), payment.getType().toString(), token);
            doThrow(new RuntimeException("Client 요청 실패"))
                    .when(paymentClient).completeRequest(any(PaymentCommand.Complete.class));

            // when
            assertThatThrownBy(() -> pointFacade.completeCharge(criteria))
                    .isInstanceOf(RuntimeException.class);

            // then
            jpaPaymentRepository.findById(payment.getId())
                    .ifPresent(p -> assertThat(p.getStatus()).isEqualTo(PaymentStatus.FAILED));
        }
    }


    @DisplayName("포인트 충전을 취소할 수 있다.")
    @Test
    void cancelCharge() {
        // given
        User user = jpaUserRepository.save(User.create("이건엽"));
        jpaPointRepository.save(Point.create(user.getId(), 1000L));
        Payment payment = jpaPaymentRepository.save(Payment.create(user.getId(), 1000L, PGType.TOSS));
        PointCriteria.CancelCharge criteria = new PointCriteria.CancelCharge(user.getId(), payment.getId());

        // when
        PointResult.CancelCharge result = pointFacade.cancelCharge(criteria);
        // then
        assertThat(result.userId()).isEqualTo(user.getId());
        assertThat(result.paymentId()).isEqualTo(payment.getId());
        assertThat(result.canceledAt()).isEqualTo(payment.getModifiedAt());
    }

}
package kr.server.pointly.application.point;

import kr.server.pointly.domain.payment.PGType;
import kr.server.pointly.domain.payment.Payment;
import kr.server.pointly.domain.point.Point;
import kr.server.pointly.domain.point.PointRepository;
import kr.server.pointly.domain.user.User;
import kr.server.pointly.infrastructure.payment.JpaPaymentRepository;
import kr.server.pointly.infrastructure.point.JpaPointRepository;
import kr.server.pointly.infrastructure.user.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PointFacadeIntegrationTest {

    @Autowired
    private PointFacade pointFacade;

    @Autowired
    private PointRepository pointRepository;

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
        PointCriteria.RequestCharge criteria = new PointCriteria.RequestCharge(user.getId(), 1000L, "TOSS");

        // when
        PointResult.RequestCharge result = pointFacade.requestCharge(criteria);
        // then
        assertThat(result.amount()).isEqualTo(1000L);
        assertThat(result.pgType()).isEqualTo("TOSS");
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
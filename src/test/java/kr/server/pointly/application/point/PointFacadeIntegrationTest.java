package kr.server.pointly.application.point;

import kr.server.pointly.domain.point.Point;
import kr.server.pointly.domain.point.PointRepository;
import kr.server.pointly.domain.user.User;
import kr.server.pointly.infrastructure.point.JpaPointRepository;
import kr.server.pointly.infrastructure.user.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PointFacadeIntegrationTest {

    @Autowired
    private PointFacade pointFacade;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private JpaPointRepository jpaPointRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @DisplayName("포인트 충전을 요청할 수 있다.")
    @Test
    void chargeRequest() {
        // given
        User user = jpaUserRepository.save(User.create("이건엽"));
        jpaPointRepository.save(Point.create(user.getId(), 1000L));
        PointCriteria.ChargeRequest criteria = new PointCriteria.ChargeRequest(user.getId(), 1000L, "TOSS");

        // when
        PointResult.ChargeRequest result = pointFacade.chargeRequest(criteria);
        // then
        assertThat(result.amount()).isEqualTo(1000L);
        assertThat(result.pgType()).isEqualTo("TOSS");
    }

}
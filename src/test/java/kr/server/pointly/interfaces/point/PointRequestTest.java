package kr.server.pointly.interfaces.point;

import kr.server.pointly.application.point.PointCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointRequestTest {

    @Nested
    class RequestCharge {
        @DisplayName("userId를 받아 Criteria를 생성할 수 있다.")
        @Test
        void toCriteria() {
            // given
            Long userId = 1L;
            PointRequest.RequestCharge request = new PointRequest.RequestCharge(1000L, "TOSS");

            // when
            PointCriteria.RequestCharge criteria = request.toCriteria(userId);

            // then
            assertThat(criteria.userId()).isEqualTo(userId);
            assertThat(criteria.amount()).isEqualTo(request.amount());
            assertThat(criteria.paymentType()).isEqualTo(request.pgType());
        }
    }

    @Nested
    class CancelCharge {
        @DisplayName("userId를 받아 Criteria를 생성할 수 있다.")
        @Test
        void toCriteria() {
            // given
            Long userId = 1L;
            PointRequest.CancelCharge request = new PointRequest.CancelCharge(1L);

            // when
            PointCriteria.CancelCharge criteria = request.toCriteria(userId);

            // then
            assertThat(criteria.userId()).isEqualTo(userId);
            assertThat(criteria.paymentId()).isEqualTo(request.paymentId());
        }
    }


}
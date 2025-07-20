package kr.server.pointly.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class PointTest {

    @Nested
    class Charge{

        @DisplayName("포인트 충전 시 원래 보유하고 있던 포인트에 충전 요청한 금액을 더한 값을 포인트로 가진다.")
        @Test
        void success() {
            // given
            Point point = Point.create(1L, 1000L);
            Long amount = 1000L;

            // when
            point.charge(amount);

            // then
            assertThat(point.getBalance()).isEqualTo(2000L);
        }

        @DisplayName("0이하의 값을 충전하려는 경우 IllegalArgumentException 발생한다.")
        @Test
        void failByZeroAmount() {
            // given
            Point point = Point.create(1L, 1000L);

            // when // then
            assertThatThrownBy(() -> point.charge(0L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("포인트 충전은 1원 이상부터 가능합니다.");
        }
    }

}
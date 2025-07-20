package kr.server.pointly.domain.point;

import kr.server.pointly.infrastructure.point.JpaPointHistoryRepository;
import kr.server.pointly.infrastructure.point.JpaPointRepository;
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
class PointServiceIntegrationTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private JpaPointRepository jpaPointRepository;

    @Autowired
    private JpaPointHistoryRepository jpaPointHistoryRepository;

    @Nested
    class Charge {

        @DisplayName("userId에 해당하는 유저의 포인트를 amount만큼 충전한다.")
        @Test
        void success() {
            // given
            Long userId = 1L;
            Long amount = 1000L;
            PointCommand.Charge command = new PointCommand.Charge(userId, amount);
            jpaPointRepository.save(Point.create(1L, 1000L));

            // when
            Point chargedPoint = pointService.charge(command);

            // then
            assertThat(chargedPoint).isNotNull();
            assertThat(chargedPoint.getBalance()).isEqualTo(2000L);
            assertThat(chargedPoint.getUserId()).isEqualTo(1L);
            List<PointHistory> historyList = jpaPointHistoryRepository.findAll();
            assertThat(historyList).hasSize(1);
            PointHistory pointHistory = historyList.getFirst();
            assertThat(pointHistory.getType()).isEqualTo(TransactionType.CHARGED);
            assertThat(pointHistory.getAmount()).isEqualTo(amount);
        }

        @DisplayName("userId에 해당하는 point를 조회하지 못한 경우 IllegalArgumentException 발생한다.")
        @Test
        void fail() {
            // given
            Long userId = 1L;
            Long amount = 1000L;
            PointCommand.Charge command = new PointCommand.Charge(userId, amount);

            // when // then
            assertThatThrownBy(() -> pointService.charge(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당되는 유저가 없습니다.");
        }
    }
}
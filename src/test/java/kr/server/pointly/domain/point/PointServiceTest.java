package kr.server.pointly.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    private PointService pointService;

    @Nested
    class Charge {
        @DisplayName("올바른 userId로 포인트 충전을 요청하면 amount만큼 충전한 후 이력과 함께 저장된다.")
        @Test
        void success() {
            // given
            Long userId = 1L;
            PointCommand.Charge command = new PointCommand.Charge(userId, 1000L);
            when(pointRepository.findByUserId(command.userId()))
                    .thenReturn(Optional.of(Point.create(userId, 0L)));
            doNothing().when(pointHistoryRepository).save(any(PointHistory.class));
            // when
            Point chargedPoint = pointService.charge(command);

            // then
            assertThat(chargedPoint).isNotNull();
            verify(pointRepository, times(1)).findByUserId(userId);
            verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
        }

        @DisplayName("포인트 충전에 실패한 경우 이력은 저장되지 않는다.")
        @Test
        void fail() {
            // given
            PointCommand.Charge command = new PointCommand.Charge(1L, 1000L);

            // when
            assertThatThrownBy(() -> pointService.charge(command));

            // then
            verify(pointHistoryRepository, never()).save(any(PointHistory.class));
        }

    }


}
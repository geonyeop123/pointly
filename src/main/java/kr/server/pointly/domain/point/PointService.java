package kr.server.pointly.domain.point;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService {


    private final PointRepository pointRepository;

    private final PointHistoryRepository pointHistoryRepository;

    @Retryable(
            retryFor = OptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2)      // 100ms → 200ms → 400ms
    )
    public Point charge(PointCommand.Charge command) {
        Point point = pointRepository.findByUserId(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 유저가 없습니다."));

        point.charge(command.amount());

        pointHistoryRepository.save(PointHistory.create(point.getId(), TransactionType.CHARGED, command.amount(), LocalDateTime.now()));

        return point;
    }

}

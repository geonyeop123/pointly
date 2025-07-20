package kr.server.pointly.infrastructure.point;

import kr.server.pointly.domain.point.PointHistory;
import kr.server.pointly.domain.point.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final JpaPointHistoryRepository jpaPointHistoryRepository;

    @Override
    public void save(PointHistory pointHistory) {
        jpaPointHistoryRepository.save(pointHistory);
    }
}

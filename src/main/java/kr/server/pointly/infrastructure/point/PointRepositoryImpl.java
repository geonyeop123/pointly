package kr.server.pointly.infrastructure.point;

import kr.server.pointly.domain.point.Point;
import kr.server.pointly.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final JpaPointRepository jpaPointRepository;

    @Override
    public Optional<Point> findByUserId(Long userId) {
        return jpaPointRepository.findByUserId(userId);
    }
}

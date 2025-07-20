package kr.server.pointly.infrastructure.point;

import kr.server.pointly.domain.point.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPointHistoryRepository extends JpaRepository<PointHistory, Long> {

}

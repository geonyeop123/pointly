package kr.server.pointly.domain.point;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import kr.server.pointly.domain.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Point extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long userId;

    private Long balance;

    @Version
    private Long version;

    private Point(Long userId, Long balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public static Point create(Long userId, Long balance) {
        return new Point(userId, balance);
    }

    public void charge(Long amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("포인트 충전은 1원 이상부터 가능합니다.");
        }
        this.balance += amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;
        return Objects.equals(id, point.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

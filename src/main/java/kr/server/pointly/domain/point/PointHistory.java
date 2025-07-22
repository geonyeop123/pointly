package kr.server.pointly.domain.point;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class PointHistory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long pointId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Long amount;

    LocalDateTime createdAt;

    private PointHistory(Long pointId, TransactionType type, Long amount, LocalDateTime createdAt) {
        this.pointId = pointId;
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static PointHistory charged(Long pointId, Long amount, LocalDateTime createdAt) {
        return new PointHistory(pointId, TransactionType.CHARGED, amount, createdAt);
    }
}

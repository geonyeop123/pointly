package kr.server.pointly.domain.payment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Long amount;

    private String paymentToken;

    private String messageCode;

    private String message;

    LocalDateTime createdAt;

    private PaymentHistory(Long paymentId, TransactionType type, Long amount, String paymentToken, String messageCode, String message, LocalDateTime createdAt) {
        this.paymentId = paymentId;
        this.type = type;
        this.amount = amount;
        this.paymentToken = paymentToken;
        this.messageCode = messageCode;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static PaymentHistory paid(Payment payment, String paymentToken, LocalDateTime createdAt) {
        if(payment.getStatus() != PaymentStatus.COMPLETED){
            throw new IllegalStateException("결제 완료 상태가 아닌 경우 결제 완료 이력을 생성할 수 없습니다.");
        }
        return new PaymentHistory(payment.getId(), TransactionType.PAID, payment.getAmount(), paymentToken, null, null, createdAt);
    }
}

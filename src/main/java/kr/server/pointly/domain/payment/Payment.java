package kr.server.pointly.domain.payment;

import jakarta.persistence.*;
import kr.server.pointly.domain.common.BaseEntity;
import kr.server.pointly.support.exception.InvalidPaymentStatusException;
import kr.server.pointly.support.exception.PaymentMismatchException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.GenerationType.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long userId;

    private Long amount;

    private String orderId;

    private String paymentToken;

    @Enumerated(EnumType.STRING)
    private PGType type;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private Payment(Long userId, Long amount, String orderId, String paymentToken, PGType type, PaymentStatus status) {
        this.userId = userId;
        this.amount = amount;
        this.orderId = orderId;
        this.paymentToken = paymentToken;
        this.type = type;
        this.status = status;
    }

    public static Payment create(Long userId, Long amount, PGType type){
        return new Payment(userId, amount, UUID.toString(), null, type, PaymentStatus.PENDING);
    }

    public void complete(Long paidAmount, PGType type, String orderId) {
        validateComplete(paidAmount, type, orderId);
        this.status = PaymentStatus.COMPLETED;
    }

    private void validateComplete(Long paidAmount, PGType type, String orderId) {
        if( this.status != PaymentStatus.PENDING){
            throw new InvalidPaymentStatusException(this.status, PaymentStatus.COMPLETED, this.id);
        }else if(!Objects.equals(paidAmount, this.amount) || type != this.type || !orderId.equals(this.orderId)){
            throw new PaymentMismatchException(this.id);
        }
    }

    public void cancel() {
        if( this.status != PaymentStatus.PENDING){
            throw new InvalidPaymentStatusException(this.status, PaymentStatus.CANCELED, this.id);
        }
        this.status = PaymentStatus.CANCELED;
    }

    public void fail() {
        if( this.status != PaymentStatus.COMPLETED){
            throw new InvalidPaymentStatusException(this.status, PaymentStatus.FAILED, this.id);
        }
        this.status = PaymentStatus.FAILED;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

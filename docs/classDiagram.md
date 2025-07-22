## 목차
- [README.md](../README.md)
- [클래스 다이어그램](classDiagram.md)
- [시퀀스 다이어그램](sequenceDiagram.md)
- [ERD](erd.md)

---
## 클래스 다이어그램

```mermaid
classDiagram
    User<--Point
    User<--Payment
    PaymentStatus<--Payment
    PaymentType<--Payment
    Point<--PointHistory
    Payment<--PaymentHistory
    PaymentTransactionType<--PaymentHistory
    PointTransactionType<--PointHistory
    class User {
        -Long id
        -String name
        -Long viewCount
        -LocalDateTime createdAt
        -LocalDateTime modifiedAt
    }
    class Point {
        -Long id
        -Long userId
        -Long balance
        -LocalDateTime createdAt
        -LocalDateTime modifiedAt
        +create(userId : Long, balance : Long) : Point
        +charge(amount : Long) : void
    }
    class Payment {
        -Long id
        -Long userId
        -Long amount
        -String orderId
        -String paymentToken
        -PGType type
        -PaymentStatus status
        -LocalDateTime createdAt
        -LocalDateTime modifiedAt
        +create(userId : Long, amount : Long, type : PGType) : Payment
        +cancel() : void
        +complete(amount : Long, type : PGType, orderId : String) : void
        -validateComplete(paidAmount : Long, type : PGType, orderId : String) : void
        +fail() : void
    }

    class PaymentStatus {
        <<enumeration>>
        PENDING
        COMPLETED
        CANCELED
        FAILED
    }

    class PaymentType {
        <<enumeration>>
        TOSS
    }

    class PaymentHistory {
        -Long id
        -Long paymentId
        -TransactionType type
        -Long amount
        -LocalDateTime createdAt
        +paid(payment : Payment, createdAt : LocalDateTime) : PaymentHistory
    }

    class PaymentTransactionType {
        <<enumeration>>
        PAID
    }
    class PointHistory{
        -Long id
        -Long pointId
        -TransactionType type
        -Long amount
        -LocalDateTime createdAt
        +charged(pointId : Long, amount : Long, LocalDateTime createdAt) : PointHistory
    }
    class PointTransactionType {
        <<enumeration>>
        CHARGE
    }


```
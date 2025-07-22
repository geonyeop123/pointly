## 목차
- [README.md](../README.md)
- [클래스 다이어그램](classDiagram.md)
- [시퀀스 다이어그램](sequenceDiagram.md)
- [ERD](erd.md)

---
## 시퀀스 다이어그램

* [1. 유저 프로필 목록 API](#프로필-목록-조회)
* [2. 유저 조회수 증가 API](#유저-조회수-증가)
* [3. 포인트 충전 요청 API](#포인트-충전-요청)
* [4. 포인트 충전 결제 완료 API](#포인트-충전-결제-완료)
* [5. 포인트 충전 결제 취소 API](#포인트-충전-결제-취소)

---

## 프로필 목록 조회
```mermaid
sequenceDiagram
    participant Client
    participant Server
    participant Database

    Client->>Server: GET /api/v1/users?sort=name&page=1&size=10
    Note over Server: 요청 파라미터(name, page, size) 검증

    Server->>Database: 정렬 및 페이지네이션(LIMIT, OFFSET) 조건으로 쿼리
    Database-->>Server: 회원 목록 및 전체 개수 반환

    Server->>Server: 응답 데이터(목록, 페이지 정보) 포맷팅
    Server-->>Client: 200 OK (목록, 페이지 정보 포함)
```
## 유저 조회수 증가
```mermaid
sequenceDiagram
    participant Client
    participant Server
    participant Database

    Client->>Server: POST /api/users/{userId}/view
    Server->>Database: 조회수 증가
	  Note over Database : UPDATE Member SET viewCount = viewCount + 1 <br/>WHERE id = :userId
    Server-->>Client: 200 OK (요청 성공)
```

## 포인트 충전 요청
```mermaid
sequenceDiagram
    participant Client
    participant Server
    participant Database

    Client->>Server: POST /api/v1/users/points/charge (userId, amount, pgType)
		Note over Server : Payment, PaymentHistory 생성
    Server->>Database : Payment, PaymentHistory 저장
		Server->>Client : 200 OK (요청 성공 paymentId, orderId)
```

## 포인트 충전 결제 완료
```mermaid
sequenceDiagram
    participant Client
		participant Server
    participant Database
    participant Toss Payments API
    participant Event

    Client->>Server: PATCH /api/v1/points/charge/approve (userId, paymentId, amount, pgType, paymentToken)
    Server->>Database : Payment 조회
    Database->>Server : Payment 반환
	  Server->>Server : Payment 검증
    alt 검증 성공
		Note over Server : Payment 갱신, PointHistory, PaymentHistory 생성, Point 충전
		Server->>Database : Point, Payment, PointHistory, PaymentHistory 저장
	  Server->>Toss Payments API : 결제 승인 요청
	  alt 승인 완료
    Toss Payments API->>Server : 승인 완료 응답
		Server->>Client : 200 OK (요청 성공)
		else 승인 실패
          Toss Payments API->>Server : 승인 실패 응답
          Server->>Database : Payment 실패 갱신
          Server->>Client : Fail (toss payments api status code)
		end
	  else 검증 실패
        Server->>Client : 400 BAD_REQUEST (요청 실패)
		end
```

## 포인트 충전 결제 취소
```mermaid
sequenceDiagram
    participant Client
    participant Server
    participant Database

    Client->>Server: PATCH /api/v1/points/charge/cancel (userId, paymentId)
    Server->>Database : Payment 조회
    Database->>Server : Payment 반환
		Note over Server : Payment 갱신 (CANCELED)
    Server->>Database : Payment 저장
		Server->>Client : 200 OK (요청 성공)
```
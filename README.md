# Payment & Point System

> 결제 및 포인트 기능을 관리하는 백엔드 시스템입니다.<br>
> `Clean Architecture` + `Layered Architecture`를 기반으로, 유지보수성과 테스트 용이성을 갖춘 구조로 설계되었습니다.

---

## 프로젝트 개요

이 시스템은 다음과 같은 기능을 제공합니다:

- 유저 목록 조회할 수 있습니다.
  - 이름순, 조회순, 등록순으로 정렬된 목록을 조회할 수 있습니다.
- 유저의 프로필 상세를 조회하는 경우 조회수가 증가합니다.
- 포인트를 충전할 수 있습니다.
  - 포인트 충전에 사용되는 결제 방법으로 토스페이먼츠를 제공합니다.

---

## Architecture

본 프로젝트는 Clean Architecture와 전통적인 Layered Architecture를 조합하여 설계되었습니다.

- `Interfaces Layer`: Web API 요청 처리
  - 해당 계층의 Data Type은 `Request`와 `Response`로 구성
- `Application Layer`: Facade 및 트랜잭션 처리
- - 해당 계층의 Data Type은 `Criteria`와 `Result`로 구성
- `Domain Layer`: 비즈니스 로직 및 도메인 모델 정의
  - 해당 계층의 Data Type은 `Command`와 `Domain`으로 구성
- `Infrastructure Layer`: DB, 외부 API 연동 등 기술 구현
  - `repositoryImpl` 등 `Domain` 계층에서 사용되는 외부 기술 구현체로 구성

> 도메인 모델에 비즈니스 로직이 포함되게 구성하며, 여러 도메인과 협력할 시 Application Layer의 Facade에서 수행합니다.

---

## Diagram

- [클래스 다이어그램](docs/classDiagram.md)
- [시퀀스 다이어그램](docs/sequenceDiagram.md)
- [ERD](docs/erd.md)

## Swagger

http://localhost:8080/swagger-ui/index.html
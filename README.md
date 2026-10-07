# 4주차 실전 프로젝트 - API 서버 구현 & 캐싱 적용

Spring Boot 기반의 상품 관리 API 서버를 구현하고,  
실제 서비스에서 필요한 페이징, 예외 처리, 환경별 설정, 헬스 체크, Redis 캐싱 등을 적용하는 프로젝트입니다.

## 🎯 학습 목표

1~3주차에서 학습한 Spring Boot, JPA, 캐싱 등의 내용을 바탕으로 실제 동작하는 API 서버를 구현합니다.

단순 CRUD 구현에 그치지 않고 다음 내용을 직접 적용하는 것을 목표로 합니다.

- REST API 설계
- Spring Boot Auto Configuration 이해
- local / prod 환경 설정 분리
- Actuator를 이용한 헬스 체크
- Pagination
- 예외 처리 및 공통 응답 형식
- Redis를 이용한 조회 API 캐싱
- 캐시 무효화

---

## 🛠 Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Gradle
- Redis

---

## 📦 Domain

상품(Product)을 관리하는 API를 구현합니다.

```text
Product
├── id
├── name
├── price
└── stock
```

---

## 📌 API

| Method | Endpoint | Description | Status |
|---|---|---|---|
| POST | `/api/v1/products` | 상품 생성 | ✅ |
| GET | `/api/v1/products/{id}` | 상품 단건 조회 | ✅ |
| GET | `/api/v1/products` | 상품 목록 조회 | ✅ |
| PATCH | `/api/v1/products/{id}` | 상품 수정 | ✅ |
| DELETE | `/api/v1/products/{id}` | 상품 삭제 | ✅ |

API URL에는 동작이 아닌 리소스를 표현하고, HTTP Method를 통해 동작을 구분합니다.

```text
POST   /api/v1/products
GET    /api/v1/products/1
PATCH  /api/v1/products/1
DELETE /api/v1/products/1
```

또한 `/api/v1` 형태로 API 버전을 명시합니다.

---

## ✅ 구현 현황

### 1. 상품 CRUD

- [x] 상품 생성
- [x] 상품 단건 조회
- [x] 상품 목록 조회
- [x] 상품 수정
- [x] 상품 삭제

Spring Data JPA의 `JpaRepository`를 이용하여 데이터에 접근합니다.

상품 수정 시에는 트랜잭션 내에서 조회한 영속 상태의 Entity를 변경하고,
JPA의 Dirty Checking(변경 감지)을 통해 UPDATE 쿼리가 실행되도록 구현했습니다.

---

### 2. Pagination

- [x] 상품 목록 Pagination 적용

Spring Data JPA의 `Pageable`과 `Page`를 이용하여 상품 목록을 페이지 단위로 조회합니다.

```http
GET /api/v1/products?page=0&size=10
```

전체 상품을 한 번에 조회하지 않고 필요한 범위만 조회할 수 있도록 구현합니다.

---

### 3. 예외 처리 및 공통 응답

- [x] 상품을 찾을 수 없는 경우 예외 처리
- [x] Custom Exception 구현
- [x] `@RestControllerAdvice`를 이용한 전역 예외 처리
- [x] 공통 성공 응답 형식 적용
- [x] 공통 에러 응답 형식 적용

현재 존재하지 않는 상품을 조회할 경우 일반 예외가 발생하므로,
이를 개선하여 적절한 HTTP Status와 에러 응답을 반환하도록 구현할 예정입니다.

예시:

```text
GET /api/v1/products/999

현재
→ 500 Internal Server Error

개선
→ 404 Not Found
```

---

### 4. Profile을 이용한 환경 설정 분리

- [x] local 환경 설정
- [x] prod 환경 설정
- [x] 환경변수를 이용한 민감 정보 관리

Spring Boot의 Profile과 외부 설정 기능을 이용하여
로컬 개발 환경과 운영 환경의 설정을 분리합니다.

```text
application.yml
application-local.yml
application-prod.yml
```

DB 비밀번호와 같은 민감 정보는 코드에 직접 작성하지 않고 환경변수를 통해 관리합니다.

---

### 5. Spring Boot Auto Configuration

- [x] Auto Configuration 동작 원리 학습
- [x] 현재 프로젝트에서 적용되는 자동 설정 확인

Spring Boot가 의존성과 설정을 기반으로 필요한 Bean과 설정을 자동으로 구성하는
Auto Configuration의 동작 원리를 학습합니다.

---

### 6. Actuator Health Check

- [x] Spring Boot Actuator 적용
- [x] Health Check Endpoint 구성

배포 이후 서버 상태를 확인할 수 있도록 Actuator를 적용합니다.

```http
GET /actuator/health
```

서버와 주요 컴포넌트의 상태를 확인하고 이후 배포 환경의 모니터링에도 활용합니다.

---

### 7. Redis Cache

- [x] Redis 연동
- [x] 상품 단건 조회 캐싱
- [x] Cache Hit / Miss 확인
- [x] 상품 수정 시 캐시 무효화
- [x] 상품 삭제 시 캐시 무효화
- [x] TTL 설정

조회가 빈번한 상품 조회 API에 Redis Cache를 적용합니다.

```text
첫 번째 조회

Client
  ↓
GET /products/1
  ↓
Redis Cache Miss
  ↓
MySQL 조회
  ↓
Redis 저장
  ↓
Response


두 번째 조회

Client
  ↓
GET /products/1
  ↓
Redis Cache Hit
  ↓
Response
```

상품 데이터가 수정되거나 삭제될 경우 기존 캐시 데이터가 남지 않도록
Cache Evict 전략도 함께 적용합니다.

---

## 📋 최종 과제

- [x] 캐싱이 적용된 API 서버 코드
- [x] `local / prod` Profile별 설정 파일
- [x] Actuator Health Check Endpoint
- [x] REST API 명세 정리 -> swagger
- [ ] 코드 리뷰
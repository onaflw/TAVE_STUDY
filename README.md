# Week 2 Assignment

## 1. JPA 연관관계 및 N+1 문제

쇼핑몰의 `Member`와 `Order`를 N:1 관계로 구현하였다.

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "member_id")
private Member member;
```

`Order` 목록을 조회한 뒤 각 주문의 `Member`에 접근하면 LAZY Loading으로 인해 Member를 조회하는 추가 쿼리가 발생한다.

테스트에서는 Order 3개를 조회했을 때 다음과 같이 총 4번의 SELECT가 발생하였다.

```text
Order 조회      1회
Member 조회     3회
-------------------
총 SELECT       4회
```

이를 Fetch Join, `@EntityGraph`, Batch Size를 사용하여 해결 및 개선하였다.

## 2. N+1 해결 방법

### Fetch Join

```java
@Query("select o from Order o join fetch o.member")
List<Order> findAllWithFetchJoin();
```

Order와 Member를 JOIN하여 한 번에 조회한다.

### @EntityGraph

```java
@EntityGraph(attributePaths = "member")
@Query("select o from Order o")
List<Order> findAllWithEntityGraph();
```

함께 조회할 연관관계를 지정하여 추가 Member 조회가 발생하지 않도록 하였다.

### Batch Size

```properties
spring.jpa.properties.hibernate.default_batch_fetch_size=100
```

Member를 하나씩 조회하는 대신 여러 ID를 `IN` 쿼리로 묶어서 조회한다.

### 쿼리 비교

| 방법 | 조회 방식 | SELECT |
|---|---|---:|
| 기본 LAZY | Order 조회 + Member 개별 조회 | 4회 |
| Fetch Join | JOIN으로 함께 조회 | 1회 |
| `@EntityGraph` | 연관 엔티티 함께 조회 | 1회 |
| Batch Size | Member를 IN 쿼리로 일괄 조회 | 2회 |

Batch Size 적용 시 실제로 Order 조회 후 Member를 `IN (...)`으로 일괄 조회하는 것을 확인하였다. :chatgpt-content-reference{index="0"}

---

## 3. 트랜잭션 격리 수준

트랜잭션 격리 수준은 여러 트랜잭션이 동시에 실행될 때 서로의 데이터에 접근할 수 있는 정도를 결정한다.

| 격리 수준 | Dirty Read | Non-Repeatable Read | Phantom Read |
|---|---|---|---|
| READ UNCOMMITTED | 발생 가능 | 발생 가능 | 발생 가능 |
| READ COMMITTED | 방지 | 발생 가능 | 발생 가능 |
| REPEATABLE READ | 방지 | 방지 | 발생 가능* |
| SERIALIZABLE | 방지 | 방지 | 방지 |

> 실제 동작은 DBMS의 구현 방식에 따라 차이가 있을 수 있다.

- **Dirty Read**: Commit되지 않은 다른 트랜잭션의 데이터를 읽는 현상
- **Non-Repeatable Read**: 같은 트랜잭션에서 같은 데이터를 다시 조회했을 때 값이 달라지는 현상
- **Phantom Read**: 같은 조건으로 다시 조회했을 때 행이 추가되거나 사라지는 현상

격리 수준이 높아질수록 데이터 일관성은 강화되지만, 동시 처리 성능에는 더 많은 제약이 생길 수 있다.

---

## 4. 정리

이번 과제를 통해 JPA의 LAZY Loading에서 발생할 수 있는 N+1 문제를 직접 확인하고, **Fetch Join, `@EntityGraph`, Batch Size**를 적용하여 쿼리 차이를 비교하였다.

또한 트랜잭션 격리 수준에 따라 발생할 수 있는 **Dirty Read, Non-Repeatable Read, Phantom Read**를 비교하였다.
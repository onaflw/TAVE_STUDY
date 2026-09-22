package com.example.week2assignment.repository;

import com.example.week2assignment.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Fetch Join
    @Query("select o from Order o join fetch o.member")
    List<Order> findAllWithFetchJoin();

    // EntityGraph
    @EntityGraph(attributePaths = "member")
    @Query("select o from Order o")
    List<Order> findAllWithEntityGraph();
}
package com.example.week2assignment.repository;

import com.example.week2assignment.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
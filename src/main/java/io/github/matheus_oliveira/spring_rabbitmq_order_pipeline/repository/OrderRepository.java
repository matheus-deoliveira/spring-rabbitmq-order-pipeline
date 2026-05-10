package io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> { 
}

package io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.controller;

import io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.model.Order;
import io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody Order order) {
        Order savedOrder = service.createOrder(order);
        return ResponseEntity.ok(savedOrder);
    }
}

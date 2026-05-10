package io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.service;

import io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.config.RabbitMQConfig;
import io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.model.Order;
import io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository _orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public Order createOrder(Order order) {
        // Define status inicial e salva no banco
        order.setStatus("PENDING");
        Order savedOrder = _orderRepository.save(order);

        // Envia para a fila do RabbitMQ
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                savedOrder
        );

        return savedOrder;
    }
}

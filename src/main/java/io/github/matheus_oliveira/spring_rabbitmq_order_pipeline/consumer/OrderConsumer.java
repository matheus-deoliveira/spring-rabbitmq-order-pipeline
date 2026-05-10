package io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.consumer;

import io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.config.RabbitMQConfig;
import io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.model.Order;
import io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderRepository repository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processOrder(Order orderMessage) throws InterruptedException {
        System.out.println("-> Mensagem recebida. Processando Pedido ID: " + orderMessage.getId());
        
        // Simula um processamento demorado
        Thread.sleep(3000); 

        // Atualiza o status no banco de dados
        Order order = repository.findById(orderMessage.getId()).orElseThrow();
        order.setStatus("PROCESSED");
        repository.save(order);

        System.out.println("-> Pedido " + order.getId() + " processado com sucesso!");
    }
}

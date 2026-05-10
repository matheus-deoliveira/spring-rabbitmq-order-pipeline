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

        Order order = repository.findById(orderMessage.getId()).orElseThrow();

        // Checa idempotência
        if ("PROCESSED".equals(order.getStatus())) {
            System.out.println("-> [IDEMPOTÊNCIA] Pedido " + order.getId() + " já processado anteriormente. Ignorando mensagem duplicada.");
            return; // Encerra o método sem lançar erro, o RabbitMQ descarta a mensagem.
        }
        
        // Simulação de falha: Se o produto tiver "erro" no nome, lançamos uma exceção
        if (orderMessage.getProduct().toLowerCase().contains("erro")) {
            System.err.println("-> Falha simulada ao processar pedido ID: " + orderMessage.getId() + ". Lançando exceção...");
            throw new RuntimeException("Erro catastrófico no banco de dados ou API externa!");
        }
        
        // Simula um processamento demorado
        System.out.println("-> Iniciando processamento pesado do Pedido ID: " + order.getId());
        Thread.sleep(3000); // Simulando a lentidão da regra de negócio

        // Atualiza o status no banco de dados
        order.setStatus("PROCESSED");
        repository.save(order);

        System.out.println("-> Pedido " + order.getId() + " processado com sucesso!");
    }
}

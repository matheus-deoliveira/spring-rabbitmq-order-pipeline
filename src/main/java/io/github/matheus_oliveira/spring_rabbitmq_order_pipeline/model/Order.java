package io.github.matheus_oliveira.spring_rabbitmq_order_pipeline.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data 
@Entity
@Table(name = "orders") // "order" é palavra reservada no Postgres
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product;
    private BigDecimal amount;
    private String status; // PENDING, PROCESSED
}

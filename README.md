# 📦 Spring RabbitMQ Order Pipeline

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.0-FF6600.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791.svg)
![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)

API de gerenciamento de pedidos baseada em uma arquitetura orientada a eventos (Event-Driven Architecture). O projeto implementa o fluxo completo de criação e processamento assíncrono de pedidos utilizando Spring Boot e RabbitMQ, focando em alta disponibilidade, resiliência e consistência de dados — padrões fundamentais para sistemas de missão crítica e do setor financeiro.

## 💡 O Problema e a Solução

Em APIs REST tradicionais, requisições que exigem integrações complexas (como gateways de pagamento ou emissão de notas fiscais) mantêm a conexão aberta, consumindo recursos e degradando a experiência do usuário. 

Este projeto resolve esse gargalo através do **desacoplamento assíncrono**:
1. A API recebe a requisição, persiste o estado inicial no banco (`PENDING`) e publica o evento em uma fila.
2. O usuário recebe a resposta em milissegundos.
3. O *Consumer* (Worker) escuta a fila e processa a regra de negócio no seu próprio ritmo, atuando como um "amortecedor" (Shock Absorber) contra picos de tráfego, garantindo que o banco de dados não seja sobrecarregado.

## 🚀 Como executar localmente

### 1. Subir a Infraestrutura (PostgreSQL e RabbitMQ)
Certifique-se de ter o Docker instalado e rodando. Na raiz do projeto, execute:
```bash
docker-compose up -d

```

*(O painel do RabbitMQ estará disponível em `http://localhost:15672` com as credenciais `admin` / `password`).*

### 2. Iniciar a Aplicação Spring Boot

Utilize o Maven Wrapper para compilar e iniciar o serviço na porta 8080:

```bash
./mvnw clean spring-boot:run

```

## 🧪 Como testar

### Requisição Simples

Envie um pedido para a API e observe os logs da aplicação processando a fila:

```bash
curl -X POST http://localhost:8080/orders \
     -H "Content-Type: application/json" \
     -d '{"product": "Teclado Keychron K3 Max", "amount": 750.00}'

```

### Stress Test (Simulação de Carga)

Para visualizar o comportamento da fila absorvendo um pico de requisições, execute o loop abaixo em seu terminal e observe o painel do RabbitMQ:

```bash
for i in {1..20}; do
  curl -s -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d "{\"product\": \"Produto de Teste $i\", \"amount\": $((i * 50)).00}" &
done

```

## 🗺️ Roadmap e Próximos Passos (Em Desenvolvimento)

Esta seção documenta a evolução da arquitetura do projeto rumo a um padrão de excelência corporativa.

* [x] **Configurar uma DLQ (Dead Letter Queue):** Em sistemas de pagamento, se uma mensagem falhar, ela não pode ser perdida nem travar a fila principal. Configurar o RabbitMQ para rotear mensagens falhas (ex: erro temporário no banco) para uma fila de "mensagens mortas" para análise e reprocessamento posterior.
* [x] **Aumentar a Concorrência (Workers):** Otimizar a vazão da fila adicionando a propriedade `spring.rabbitmq.listener.simple.concurrency=5` no `application.properties`, permitindo o processamento paralelo de múltiplos pedidos simultaneamente.
* [x] **Idempotência no Consumo:** Adicionar uma trava de segurança no `OrderConsumer` para verificar se o pedido já consta com status `PROCESSED` no banco de dados antes de executar a regra de negócio. Isso previne cobranças duplicadas em caso de instabilidade na rede e reentrega de mensagens.
* [x] **CI/CD e Segurança (DevSecOps):** Desenvolver um workflow utilizando GitHub Actions para automatizar a execução de testes automatizados e integrar o Trivy para realizar varreduras (scans) de vulnerabilidades na imagem Docker antes do deploy.
# 💳 Gateway de Pagamentos de Alta Disponibilidade

Este projeto é um ecossistema de processamento de pagamentos robusto, focado em resiliência, mensageria assíncrona e arquitetura de microsserviços. O objetivo é garantir que cada transação seja registrada com segurança e processada de forma escalável.

## 🚀 Tecnologias e Ferramentas Utilizadas

* **Java 17 & Spring Boot 3**: Coração da aplicação e lógica de negócio.
* **Apache Kafka & Zookeeper**: Mensageria distribuída para processamento assíncrona de pagamentos.
* **PostgreSQL**: Banco de dados relacional para persistência de transações e logs.
* **Docker & Docker Compose**: Orquestração de toda a infraestrutura (Banco, Kafka, Zookeeper, UI).
* **Resilience4j**: Implementação de padrões de resiliência como **Circuit Breaker** e **Retry**.
* **Postman**: Validação e testes de endpoints da API.
* **Kafka UI**: Interface visual para monitoramento de tópicos e mensagens em tempo real.

* <img width="1600" height="873" alt="image" src="https://github.com/user-attachments/assets/ad870dc5-6eb7-4fe5-ad07-13a8be60e96d" />


## 🏗️ Arquitetura e Fluxo

1.  **Entrada**: O cliente (via Postman) envia uma requisição POST com os dados do pagamento.
2.  **Persistência**: O Spring Boot recebe os dados e salva no **PostgreSQL** com status `PENDING`.
3.  **Resiliência**: O **Circuit Breaker** monitora a saúde do sistema. Se o Kafka estiver fora do ar, o circuito abre para proteger a aplicação.
4.  **Mensageria**: O `PaymentProducer` publica o evento de pagamento no **Apache Kafka**.
5.  **Monitoramento**: As mensagens podem ser visualizadas em tempo real através do **Kafka UI**.

## 🛠️ Como Executar o Projeto

### Pré-requisitos
* Docker Desktop instalado.
* WSL 2 atualizado (`wsl --update`).

* <img width="1600" height="884" alt="image" src="https://github.com/user-attachments/assets/36966c77-cad4-4eb2-8fa7-6518c63d962e" />


### Passos
1.  Clone o repositório.
2.  No terminal do projeto, suba a infraestrutura:
    ```bash
    docker-compose up -d
    ```
3.  Certifique-se de que os containers do Postgres e Kafka estão rodando (bolinha verde no Docker).
4.  Execute a aplicação Spring Boot pelo IntelliJ.
5.  Acesse o Kafka UI em `http://localhost:8081` para monitorar as mensagens.

6.  <img width="1600" height="603" alt="image" src="https://github.com/user-attachments/assets/7a7d962c-49a6-4fc9-835b-95efd7be604c" />


## 🧠 Aprendizados Chave
* Configuração de infraestrutura complexa via Docker.
* Tratamento de falhas críticas em sistemas distribuídos.
* Conceitos de idempotência para evitar cobranças duplicadas.
* Resolução de problemas de ambiente e performance em hardware limitado.

* <img width="1600" height="874" alt="image" src="https://github.com/user-attachments/assets/ff8ab694-9d3f-4f0c-b57d-791902e019ee" />

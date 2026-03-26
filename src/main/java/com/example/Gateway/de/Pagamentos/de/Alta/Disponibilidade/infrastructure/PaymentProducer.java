package com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.infrastructure;

import com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentEvent(Payment payment) {
        // Envia o objeto pagamento para o tópico "payments-topic"
        kafkaTemplate.send("payments-topic", payment.getIdempotencyKey(), payment);
        System.out.println(">>> Mensagem enviada para o Kafka: " + payment.getIdempotencyKey());
    }

}

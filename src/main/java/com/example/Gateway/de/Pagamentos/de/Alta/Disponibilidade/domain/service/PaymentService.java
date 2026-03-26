package com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.domain.service;

import com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.controller.dto.PaymentRequestDTO;
import com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.domain.model.Payment;
import com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.domain.model.PaymentStatus;
import com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.domain.repository.PaymentRepository;
import com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.infrastructure.PaymentProducer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer; // O Spring injeta isso automaticamente via Lombok

    @CircuitBreaker(name = "paymentCB", fallbackMethod = "fallbackPayment")
    @Retry(name = "paymentRetry")
    public Payment processPayment(String key, PaymentRequestDTO paymentRequestDTO) {

        Payment payment = paymentRepository.findByIdempotencyKey(key)
                .orElseGet(() -> {
                    Payment newPayment = new Payment();
                    newPayment.setAmount(paymentRequestDTO.amount());
                    newPayment.setCurrency(paymentRequestDTO.currency());
                    newPayment.setIdempotencyKey(key);
                    newPayment.setStatus(PaymentStatus.PENDING);
                    newPayment.setCreatedAt(LocalDateTime.now());
                    return paymentRepository.save(newPayment);
                });

        // 1. Executa a lógica (Pode dar erro aleatório ou aprovar/rejeitar)
        Payment processedPayment = executePaymentLogic(payment);

        // 2. ENVIA PARA O KAFKA (O pulo do gato para Alta Disponibilidade)
        paymentProducer.sendPaymentEvent(processedPayment);

        return processedPayment;
    }

    private Payment executePaymentLogic(Payment payment) {
        if (Math.random() > 0.5) throw new RuntimeException("Servidor da Operadora instável!");

        if (payment.getAmount().compareTo(new java.math.BigDecimal("1000")) > 0) {
            payment.setStatus(PaymentStatus.REJECTED);
        } else {
            payment.setStatus(PaymentStatus.APPROVED);
        }
        return paymentRepository.save(payment);
    }

    // Caso o Circuit Breaker abra, também avisamos o Kafka sobre a falha
    public Payment fallbackPayment(String key, PaymentRequestDTO dto, Throwable e) {
        System.err.println(" CIRCUITO ABERTO / RETRY FALHOU! Motivo: " + e.getMessage());

        Payment errorPayment = new Payment();
        errorPayment.setAmount(dto.amount());
        errorPayment.setCurrency(dto.currency());
        errorPayment.setIdempotencyKey(key);
        errorPayment.setStatus(PaymentStatus.ERROR);
        errorPayment.setCreatedAt(LocalDateTime.now());

        // OPCIONAL: Enviar para o Kafka mesmo em caso de erro para auditoria
        paymentProducer.sendPaymentEvent(errorPayment);

        return errorPayment;
    }
}
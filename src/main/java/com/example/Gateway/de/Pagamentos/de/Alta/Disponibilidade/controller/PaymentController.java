package com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.controller;

import com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.controller.dto.PaymentRequestDTO;
import com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.domain.model.Payment;
import com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.domain.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> create(
            @RequestHeader("X-Idempotency-key") String key,
            @Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        return ResponseEntity.ok(paymentService.processPayment(key, paymentRequestDTO));
    }

}

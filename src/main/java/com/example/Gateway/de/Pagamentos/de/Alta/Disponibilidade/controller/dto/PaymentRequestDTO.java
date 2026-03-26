package com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.controller.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PaymentRequestDTO (
        @NotNull @Positive BigDecimal amount,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotBlank @Pattern(regexp = "\\d{16}") String cardNumber,
        @NotBlank String cvv,
        @NotBlank String holderName
) {}





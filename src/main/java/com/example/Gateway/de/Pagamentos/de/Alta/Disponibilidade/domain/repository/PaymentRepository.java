package com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.domain.repository;

import com.example.Gateway.de.Pagamentos.de.Alta.Disponibilidade.domain.model.Payment;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByIdempotencyKey (String idempotencyKey);
}

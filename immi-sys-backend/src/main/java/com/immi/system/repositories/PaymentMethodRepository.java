package com.immi.system.repositories;

import com.immi.system.models.PaymentMethodModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodModel, Long> {
    
}

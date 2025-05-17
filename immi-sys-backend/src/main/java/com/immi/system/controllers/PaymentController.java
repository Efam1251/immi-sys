package com.immi.system.controllers;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.PaymentDTO;
import com.immi.system.models.PaymentModel;
import com.immi.system.services.PaymentService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common/payment")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payment-methods")
    @ResponseBody
    public List<DropDownDTO> getAllPaymentMethods() {
        return paymentService.findAll().stream()
            .map(category -> new DropDownDTO(category.getId(), category.getName()))
            .collect(Collectors.toList());
    }
    
    @GetMapping("/payments-by-invoice")
    public ResponseEntity<?> getPaymentsByInvoice(@RequestParam Long invoiceNumber) {
        try {
            List<PaymentModel> payments = paymentService.findByInvoiceNumberAndCancelledFalse(invoiceNumber);
            List<PaymentDTO> paymentDTOs = payments.stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(paymentDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error retrieving payments for invoice #" + invoiceNumber);
        }
    }
    
    @PostMapping("/save")
    public ResponseEntity<?> applyPayment(@RequestBody PaymentModel paymentRequest) {
        return paymentService.applyPayment(paymentRequest);
    }
    
    @DeleteMapping("/payments/{id}")
    public ResponseEntity<?> cancelPayment(@PathVariable Long id) {
        try {
            paymentService.cancelPayment(id); // implement soft delete or refund logic as needed
            return ResponseEntity.ok("Payment cancelled.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error cancelling payment.");
        }
    }

    
}

package com.immi.system.services;

import com.immi.system.enums.InvoiceStatusEnum;
import com.immi.system.models.InvoiceModel;
import com.immi.system.models.InvoiceStatusModel;
import com.immi.system.models.PaymentMethodModel;
import com.immi.system.models.PaymentModel;
import com.immi.system.repositories.InvoiceRepository;
import com.immi.system.repositories.InvoiceStatusRepository;
import com.immi.system.repositories.PaymentMethodRepository;
import com.immi.system.repositories.PaymentRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceStatusRepository invoiceStatusRepository;
    
    public List<PaymentMethodModel> findAll() {
        return paymentMethodRepository.findAll();
    }
    
    public List<PaymentModel> findByInvoiceNumberAndCancelledFalse(Long invoiceNumber) {
        return paymentRepository.findByInvoiceNumberAndCancelledFalse(invoiceNumber);
    }    

    public ResponseEntity<?> applyPayment(PaymentModel paymentRequest) {
        Logger logger = LoggerFactory.getLogger(getClass());

        try {
            logger.info("Received payment request: {}", paymentRequest);

            // Find the invoice by invoice_number
            InvoiceModel invoice = invoiceRepository.findByInvoiceNumber(paymentRequest.getInvoiceNumber());
            if (invoice == null) {
                logger.warn("Invoice not found for invoiceNumber: {}", paymentRequest.getInvoiceNumber());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Invoice not found"));
            }

            // Get the past total paid
            BigDecimal pastTotalPaid = paymentRepository.getTotalPaymentsForInvoice(paymentRequest.getInvoiceNumber());
            logger.info("Past total paid: {}", pastTotalPaid);

            BigDecimal currentTotalPaid = pastTotalPaid.add(paymentRequest.getAmount());
            logger.info("Current total paid after this transaction: {}", currentTotalPaid);

            // Check if the payment exceeds the invoice amount
            if (currentTotalPaid.compareTo(invoice.getTotalAmount()) > 0) {
                logger.warn("Payment exceeds invoice total: {} > {}", currentTotalPaid, invoice.getTotalAmount());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Payment exceeds invoice total amount"));
            }

            // Save the payment record
            PaymentModel newPayment = new PaymentModel();
            newPayment.setInvoiceNumber(paymentRequest.getInvoiceNumber());
            newPayment.setPaymentDate(paymentRequest.getPaymentDate());
            newPayment.setPaymentMethod(paymentRequest.getPaymentMethod());
            newPayment.setAmount(paymentRequest.getAmount());
            newPayment.setNotes(paymentRequest.getNotes());

            logger.info("Saving payment: {}", newPayment);
            paymentRepository.save(newPayment);

            // Recalculate the total paid
            BigDecimal totalPaid = paymentRepository.getTotalPaymentsForInvoice(paymentRequest.getInvoiceNumber());
            logger.info("Total paid after saving payment: {}", totalPaid);
            
            // Determine new invoice status
            InvoiceStatusEnum status;
            
            if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
                status = InvoiceStatusEnum.UNPAID;
            } else if (totalPaid.compareTo(invoice.getTotalAmount()) < 0) {
                status = InvoiceStatusEnum.PARTIAL;
            } else {
                status = InvoiceStatusEnum.PAID;
            }
            
             //(Optional) Handle overdue logic if you want to:
             if (invoice.getDueDate().isBefore(LocalDate.now()) && (status == InvoiceStatusEnum.UNPAID || status == InvoiceStatusEnum.PARTIAL)) {
                 status = InvoiceStatusEnum.OVERDUE;
             }

            // Set the new status
            invoice.setInvoiceStatus(status);

            invoiceRepository.save(invoice);

            return ResponseEntity.ok(Map.of("message", "Payment applied successfully", "newStatus", invoice.getInvoiceStatus()));

        } catch (Exception e) {
            logger.error("Error processing payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error processing payment", "error", e.getMessage()));
        }
    }
    
    public void cancelPayment(Long id) {
        PaymentModel payment = paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
        payment.setCancelled(true);
        paymentRepository.save(payment);
    }

}

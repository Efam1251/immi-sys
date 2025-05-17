package com.immi.system.controllers;

import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.InvoiceDTO;
import com.immi.system.DTOs.InvoiceListDTO;
import com.immi.system.DTOs.InvoiceProfitDTO;
import com.immi.system.models.CustomerModel;
import com.immi.system.models.InvoiceDetailModel;
import com.immi.system.models.InvoiceModel;
import com.immi.system.services.CustomerService;
import com.immi.system.services.InvoiceService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/common/invoice")
public class InvoiceController {
    
    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping
    public List<InvoiceModel> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }
    
    @GetMapping("/form")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@RequestParam("id") Long invoiceId) {
        InvoiceModel invoice = invoiceId > 0 ? invoiceService.getInvoiceById(invoiceId) : new InvoiceModel();
        return invoice != null ? ResponseEntity.ok(invoiceService.convertToDTO(invoice)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/last-invoice")
    public ResponseEntity<Long> getLastInvoiceNumber() {
        Long lastInvoice = invoiceService.findMaxInvoiceNumber(); // Get the highest invoice number
        return ResponseEntity.ok(lastInvoice != null ? lastInvoice : 0L);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<InvoiceListDTO>> getInvoiceList() {
        return ResponseEntity.ok(invoiceService.getInvoiceList());
    }
    
    @GetMapping("/open-balance")
    public ResponseEntity<BigDecimal> getInvoiceOpenBalance(@RequestParam("invoiceNumber") Long invoiceNumber) {
        BigDecimal openBalance = invoiceService.getInvoiceOpenBalance(invoiceNumber);
        return ResponseEntity.ok(openBalance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/invoice-status-list")
    public ResponseEntity<List<DropDownDTO>> getInvoiceStatuses() {
        List<DropDownDTO> statuslistDto = invoiceService.getInvoiceStatusList();
        return new ResponseEntity<>(statuslistDto, HttpStatus.OK);
    }
    
    @PostMapping("/submit")
    public ResponseEntity<?> submitInvoice(@RequestBody @Valid InvoiceModel invoice, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        if (invoice.getInvoiceId() != null) {
            // If it's an update, return a consistent response
            try {
                InvoiceModel updatedInvoice = updateInvoice(invoice);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Invoice updated successfully");
                response.put("invoiceNumber", updatedInvoice.getInvoiceNumber());
                return ResponseEntity.ok(response);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            // If it's a new invoice, generate a new invoice number
            Long lastInvoice = invoiceService.findMaxInvoiceNumber();
            Long nextInvoiceNumber = (lastInvoice != null) ? lastInvoice + 1 : 1L;
            invoice.setInvoiceNumber(nextInvoiceNumber);

            try {
                createInvoice(invoice);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Invoice saved successfully");
                response.put("invoiceNumber", nextInvoiceNumber);
                return ResponseEntity.ok(response);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }
    
    // Handle validation errors
    private ResponseEntity<String> handleValidationErrors(BindingResult bindingResult) {
        StringBuilder errorMessages = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessages.append(error.getDefaultMessage()).append("\n");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages.toString());
    }
    
    private InvoiceModel updateInvoice(InvoiceModel invoice) {
        InvoiceModel existingInvoice = invoiceService.getInvoiceById(invoice.getInvoiceId());
        if (existingInvoice == null) {
            throw new IllegalArgumentException("Invoice not found");
        }

        updateInvoiceFields(existingInvoice, invoice);

        // Update invoice details (ensure proper linking)
        if (updateInvoiceDetails(invoice, existingInvoice)) {
            return invoiceService.saveInvoice(existingInvoice);  // Returning the updated invoice
        }

        throw new IllegalArgumentException("Invalid invoice details");
    }

    // Update fields for an existing invoice
    private void updateInvoiceFields(InvoiceModel existingInvoice, InvoiceModel invoice) {
        existingInvoice.setInvoiceDate(invoice.getInvoiceDate());
        existingInvoice.setDueDate(invoice.getDueDate());
        existingInvoice.setInvoiceStatus(invoice.getInvoiceStatus());
        
        existingInvoice.setBillingAddress(invoice.getBillingAddress());
        existingInvoice.setCustomerEmail(invoice.getCustomerEmail());
        existingInvoice.setCustomerPhone(invoice.getCustomerPhone());
        existingInvoice.setSubtotal(invoice.getSubtotal());
        existingInvoice.setTaxAmount(invoice.getTaxAmount());
        existingInvoice.setTotalAmount(invoice.getTotalAmount());
        existingInvoice.setNotes(invoice.getNotes());

        if (invoice.getCustomer() != null && invoice.getCustomer().getCustomerId() != null) {
            CustomerModel customer = customerService.findById(invoice.getCustomer().getCustomerId());
            existingInvoice.setCustomer(customer);
        }
    }

    // Validate and update invoice details
    private boolean updateInvoiceDetails(InvoiceModel invoice, InvoiceModel existingInvoice) {
        if (invoice.getInvoiceDetails() != null) {
            // Clear the existing details to ensure the list is updated correctly
            existingInvoice.getInvoiceDetails().clear();

            for (InvoiceDetailModel detail : invoice.getInvoiceDetails()) {
                // Ensure invoice reference is set for new details
                detail.setInvoice(existingInvoice);

                // Validate each invoice detail
                if (!isValidInvoiceDetail(detail)) {
                    return false;
                }

                // Add the valid details to the existingInvoice
                existingInvoice.getInvoiceDetails().add(detail);
            }
        }
        return true;
    }

    // Validate invoice detail fields
    private boolean isValidInvoiceDetail(InvoiceDetailModel detail) {
        if (detail.getService() == null || detail.getService().getServiceId() == null) {
            return false;
        }
        if (detail.getReference() == null || detail.getReference().isBlank()) {
            return false;
        }
        if (detail.getQuantity() == null || detail.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if (detail.getUnitOfMeasure() == null || detail.getUnitOfMeasure().getId() == null) {
            return false;
        }
        if (detail.getUnitPrice() == null || detail.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        if (detail.getDiscountAmount() != null && detail.getDiscountAmount().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        return true;
    }
    
    // Create a new invoice
    private InvoiceModel createInvoice(InvoiceModel invoice) {
        if (invoice.getInvoiceDetails() != null) {
            for (InvoiceDetailModel detail : invoice.getInvoiceDetails()) {
                detail.setInvoice(invoice);
                if (!isValidInvoiceDetail(detail)) {
                    throw new IllegalArgumentException("Invalid invoice detail data");
                }
            }
        }
        return invoiceService.saveInvoice(invoice);
    }


    @GetMapping("/invoice-profit")
    public List<InvoiceProfitDTO> getInvoiceProfitReport() {
        return invoiceService.getInvoiceProfitReport();
    }
    
    @PostMapping("/email-invoice")
    public ResponseEntity<String> emailInvoiceWithAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String customerEmail,
            @RequestParam("subject") String subject,   // Add subject parameter
            @RequestParam("message") String message    // Add message parameter
    ) throws IOException {
        boolean emailSent = invoiceService.sendInvoiceByEmail(file, customerEmail, subject, message);

        if (emailSent) {
            return ResponseEntity.ok("Invoice emailed successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send invoice.");
        }
    }

}

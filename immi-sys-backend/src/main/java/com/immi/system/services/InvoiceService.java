package com.immi.system.services;

import com.immi.system.DTOs.CustomerDTO;
import com.immi.system.DTOs.DropDownDTO;
import com.immi.system.DTOs.InvoiceDTO;
import com.immi.system.DTOs.InvoiceDetailDTO;
import com.immi.system.DTOs.InvoiceListDTO;
import com.immi.system.DTOs.InvoiceProfitDTO;
import com.immi.system.DTOs.ServiceDTO;
import com.immi.system.DTOs.UnitOfMeasureDTO;
import com.immi.system.enums.InvoiceStatusEnum;
import com.immi.system.models.InvoiceDetailModel;
import com.immi.system.models.InvoiceModel;
import com.immi.system.repositories.InvoiceRepository;
import com.immi.system.repositories.InvoiceStatusRepository;
import com.immi.system.repositories.PaymentRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class InvoiceService {
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private InvoiceStatusRepository invoiceStatusRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private JavaMailSender mailSender;
 
    public List<InvoiceModel> getAllInvoices() {
        return invoiceRepository.findAll();
    }
    
    public InvoiceModel getInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }
    
    public BigDecimal getInvoiceOpenBalance(Long invoiceNumber) {
        // Fetch the invoice by its number
        InvoiceModel invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
        // Calculate the total amount paid for this invoice
        BigDecimal totalPaid = paymentRepository.getTotalPaymentsForInvoice(invoiceNumber);
        if (totalPaid == null) {
            totalPaid = BigDecimal.ZERO;
        }
        
        // Calculate the open balance
        return invoice.getTotalAmount().subtract(totalPaid);
    }

    public InvoiceModel saveInvoice(InvoiceModel invoice) {
        // Get total amount paid for this invoice
        BigDecimal totalPaid = paymentRepository.getTotalPaymentsForInvoice(invoice.getInvoiceNumber());

        if (totalPaid == null) {
            totalPaid = BigDecimal.ZERO;
        }

        BigDecimal totalAmount = invoice.getTotalAmount();

        // Determine status based on total paid
        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            invoice.setInvoiceStatus(InvoiceStatusEnum.UNPAID);
        } else if (totalPaid.compareTo(totalAmount) < 0) {
            invoice.setInvoiceStatus(InvoiceStatusEnum.PARTIAL);
        } else {
            invoice.setInvoiceStatus(InvoiceStatusEnum.PAID);
        }
     
        return invoiceRepository.save(invoice);
    }



    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    public Long findMaxInvoiceNumber() {
        return invoiceRepository.findMaxInvoiceNumber();
    }
    
    public List<InvoiceListDTO> getInvoiceList() {
        List<InvoiceModel> invoices = invoiceRepository.findAll();
        List<InvoiceListDTO> invoiceDTOs = new ArrayList<>();

        for (InvoiceModel invoice : invoices) {
            // Calculate the total payments for the invoice
            BigDecimal totalPayments = paymentRepository.sumPaymentsByInvoiceNumber(invoice.getInvoiceNumber());

            // Calculate the open balance
            BigDecimal openBalance = invoice.getTotalAmount().subtract(totalPayments != null ? totalPayments : BigDecimal.ZERO);

            // Create DTO and set openBalance
            InvoiceListDTO dto = new InvoiceListDTO();
            dto.setInvoiceId(invoice.getInvoiceId());
            dto.setInvoiceNumber(invoice.getInvoiceNumber());
            dto.setCustomerFullName(invoice.getCustomer().getFirstName()+" "+invoice.getCustomer().getLastName());
            dto.setInvoiceDate(invoice.getInvoiceDate());
            dto.setTotalAmount(invoice.getTotalAmount());
            dto.setOpenBalance(openBalance);
            dto.setStatus(invoice.getInvoiceStatus() != null ? invoice.getInvoiceStatus().name() : null);

            // Add DTO to the list
            invoiceDTOs.add(dto);
        }

        return invoiceDTOs;
    }

    public List<DropDownDTO> getInvoiceStatusList() {
        return invoiceStatusRepository.findAll().stream()
            .map(invoiceStatus -> new DropDownDTO(invoiceStatus.getId(), invoiceStatus.getName()))
            .collect(Collectors.toList());
    }
    
    public InvoiceDTO convertToDTO(InvoiceModel invoice) {
        if (invoice == null) {
            return null;
        }
        
        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceId(invoice.getInvoiceId());
        
        // Convert Customer to DTO
        CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCustomerId(invoice.getCustomer().getCustomerId());
            customerDTO.setFirstName(invoice.getCustomer().getFirstName());
            customerDTO.setLastName(invoice.getCustomer().getLastName());
            customerDTO.setPhone(invoice.getCustomer().getPhone());
            customerDTO.setEmail(invoice.getCustomer().getEmail());
        dto.setCustomer(customerDTO); // Set full object

        dto.setBillingAddress(invoice.getBillingAddress());
        dto.setCustomerEmail(invoice.getCustomerEmail());
        dto.setCustomerPhone(invoice.getCustomerPhone());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setInvoiceDate(invoice.getInvoiceDate());
        dto.setDueDate(invoice.getDueDate());
        dto.setSubtotal(invoice.getSubtotal());
        dto.setDiscountAmount(invoice.getDiscountAmount());
        dto.setTaxAmount(invoice.getTaxAmount());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setNotes(invoice.getNotes());
        dto.setInvoiceStatus(invoice.getInvoiceStatus());
                        
        List<InvoiceDetailDTO> detailsDTO = invoice.getInvoiceDetails().stream().map(detail -> {
            InvoiceDetailDTO detailDTO = new InvoiceDetailDTO();
            detailDTO.setIdInvoiceDetail(detail.getIdInvoiceDetail());
            detailDTO.setReference(detail.getReference());
            detailDTO.setQuantity(detail.getQuantity());
            
            // Convert UnitOfMeasure to DTO
            UnitOfMeasureDTO unitDTO = new UnitOfMeasureDTO();
                unitDTO.setId(detail.getUnitOfMeasure().getId());
                unitDTO.setName(detail.getUnitOfMeasure().getName());
            detailDTO.setUnitOfMeasure(unitDTO); // Set full object
            
            // Convert Service to DTO
            ServiceDTO serviceDTO = new ServiceDTO();
                serviceDTO.setServiceId(detail.getService().getServiceId());
                serviceDTO.setServiceName(detail.getService().getServiceName());
                serviceDTO.setServiceDescription(detail.getService().getServiceDescription());
                serviceDTO.setUnitPrice(detail.getService().getUnitPrice());
            detailDTO.setService(serviceDTO); // Set full object
            
            detailDTO.setUnitPrice(detail.getUnitPrice());
            detailDTO.setDiscountAmount(detail.getDiscountAmount());
            detailDTO.setLineTotal(detail.getLineTotal());
            return detailDTO;
        }).collect(Collectors.toList());
        
        dto.setInvoiceDetails(detailsDTO);
        return dto;
    }

    public boolean sendInvoiceByEmail(MultipartFile file, String customerEmail, String subject, String message) throws IOException {
        try {
            // Create the email message
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Set the email parameters
            helper.setTo(customerEmail);
            helper.setSubject(subject); // Use the passed subject
            helper.setText(message);     // Use the passed message

            // Attach the invoice file
            InputStreamSource attachment = new ByteArrayResource(file.getBytes());
            helper.addAttachment(file.getOriginalFilename(), attachment);

            // Send the email
            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException | IOException e) {
            return false;
        }
    }
    
    public List<InvoiceProfitDTO> getInvoiceProfitReport() {
        List<InvoiceModel> invoices = invoiceRepository.findAllWithDetailsAndCustomer(); // you'll define this
        
        
        List<InvoiceProfitDTO> report = new ArrayList<>();

        for (InvoiceModel invoice : invoices) {
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal income = BigDecimal.ZERO;
            BigDecimal expense = BigDecimal.ZERO;

            for (InvoiceDetailModel detail : invoice.getInvoiceDetails()) {
                String type = detail.getService().getIsIncomeService();
                BigDecimal lineTotal = detail.getUnitPrice().multiply(detail.getQuantity());

                if ("Business_Fee".equalsIgnoreCase(type)) {
                    income = income.add(lineTotal);
                } else if ("External_Fee".equalsIgnoreCase(type)) {
                    expense = expense.add(lineTotal);
                }
                total = total.add(lineTotal);
            }
            
            BigDecimal totalPaid = paymentRepository.sumPaymentsByInvoiceNumber(invoice.getInvoiceNumber());
            if (totalPaid == null) totalPaid = BigDecimal.ZERO;

            InvoiceProfitDTO dto = new InvoiceProfitDTO();
            dto.setInvoiceNumber(invoice.getInvoiceNumber());
            dto.setCustomerName(invoice.getCustomer().getFirstName()+" "+invoice.getCustomer().getLastName());
            dto.setInvoiceDate(invoice.getInvoiceDate());
            dto.setIncome(total);
            dto.setCustomerFees(expense);
            dto.setAmountPaid(totalPaid);
            dto.setOpenBalance(total.subtract(totalPaid));
            dto.setProfit(income);

            report.add(dto);
        }

        return report;
    }
   
}

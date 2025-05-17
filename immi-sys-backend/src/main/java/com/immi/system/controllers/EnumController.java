package com.immi.system.controllers;

import com.immi.system.enums.FeeTypeEnum;
import com.immi.system.DTOs.EnumValueDTO;
import com.immi.system.enums.CitizenshipStatusEnum;
import com.immi.system.enums.FilingStatusEnum;
import com.immi.system.enums.InvoiceStatusEnum;
import com.immi.system.enums.StatusEnum;
import com.immi.system.enums.ApplicationEventTypeEnum;
import com.immi.system.utils.EnumUtils;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common/enums")
public class EnumController {

    @GetMapping("/status")
    public ResponseEntity<List<EnumValueDTO>> getStatus() {
        return ResponseEntity.ok(EnumUtils.convertEnumToDTO(StatusEnum.class));
    }
    
    @GetMapping("/tax_filing_status")
    public ResponseEntity<List<EnumValueDTO>> getTaxFilingStatuses() {
        return ResponseEntity.ok(EnumUtils.convertEnumToDTO(FilingStatusEnum.class));
    }
    
    @GetMapping("/invoice_Status_List")
    public ResponseEntity<List<EnumValueDTO>> getInvoiceStatus() {
        return ResponseEntity.ok(EnumUtils.convertEnumToDTO(InvoiceStatusEnum.class));
    }
    
    @GetMapping("/citizenship_status_list")
    public ResponseEntity<List<EnumValueDTO>> getCitizenshipStatus() {
        return ResponseEntity.ok(EnumUtils.convertEnumToDTO(CitizenshipStatusEnum.class));
    }
    
    @GetMapping("/fee_type")
    public ResponseEntity<List<EnumValueDTO>> getFeeTypeStatus() {
        return ResponseEntity.ok(EnumUtils.convertEnumToDTO(FeeTypeEnum.class));
    }
    
    @GetMapping("/application-event-type")
    public ResponseEntity<List<EnumValueDTO>> getEventProcessStatus() {
        return ResponseEntity.ok(EnumUtils.convertEnumToDTO(ApplicationEventTypeEnum.class));
    }
    
}

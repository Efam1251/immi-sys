package com.immi.system.services;

import com.immi.system.DTOs.SimpleDTO;
import com.immi.system.models.UscisOfficeModel;
import com.immi.system.repositories.UscisOfficeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UscisOfficeService {

    @Autowired
    private UscisOfficeRepository uscisOfficeRepo;

    public SimpleDTO createUscisOffice(SimpleDTO dto) {
        if (uscisOfficeRepo.findByName(dto.getName()) != null) {
            throw new RuntimeException("USCIS Office already exists");
        }

        UscisOfficeModel office = new UscisOfficeModel();
        office.setName(dto.getName());

        return mapToDTO(uscisOfficeRepo.save(office));
    }

    public List<SimpleDTO> getAllUscisOffices() {
        return uscisOfficeRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SimpleDTO getUscisOfficeById(Long id) {
        return mapToDTO(uscisOfficeRepo.findById(id).orElseThrow(() -> new RuntimeException("USCIS Office not found")));
    }

    public SimpleDTO getNextUscisOffice(Long id) {
        return mapToDTO(uscisOfficeRepo.findFirstByIdGreaterThanOrderByIdAsc(id));
    }

    public SimpleDTO getPreviousUscisOffice(Long id) {
        return mapToDTO(uscisOfficeRepo.findFirstByIdLessThanOrderByIdDesc(id));
    }

    public SimpleDTO getFirstUscisOffice() {
        return mapToDTO(uscisOfficeRepo.findFirstByOrderByIdAsc());
    }

    public SimpleDTO getLastUscisOffice() {
        return mapToDTO(uscisOfficeRepo.findFirstByOrderByIdDesc());
    }

    public SimpleDTO updateUscisOffice(Long id, SimpleDTO dto) {
        UscisOfficeModel office = uscisOfficeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("USCIS Office not found"));

        office.setName(dto.getName());

        return mapToDTO(uscisOfficeRepo.save(office));
    }

    public void deleteUscisOffice(Long id) {
        uscisOfficeRepo.deleteById(id);
    }

    private SimpleDTO mapToDTO(UscisOfficeModel office) {
        SimpleDTO dto = new SimpleDTO();
        dto.setId(office.getId());
        dto.setName(office.getName());
        return dto;
    }
}

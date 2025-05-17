package com.immi.system.services;

import com.immi.system.DTOs.FormDTO;
import com.immi.system.models.FormModel;
import com.immi.system.repositories.FormRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FormService {

    private final FormRepository formRepository;

    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    @Transactional
    public FormModel saveRecord(FormModel record, MultipartFile file) throws IOException {
        
        Long id = record.getId();
        String name = record.getName();
        String description = record.getDescription();
        String location = record.getLocation();

        FormModel formDocument = new FormModel();

        if (id != null && id > 0) {
            Optional<FormModel> existingDocument = formRepository.findById(id);
            if (existingDocument.isPresent()) {
                formDocument = existingDocument.get(); // Update existing record
            }
        }
        
        if (file != null) {
            formDocument.setLocation(file.getOriginalFilename());
        } else {
            formDocument.setLocation(location);
        }

        formDocument.setName(name);
        formDocument.setDescription(description);
        formDocument = formRepository.save(formDocument);

        if (file != null && !file.isEmpty()) {
            Path copyLocation = Paths.get("./documents/template_forms/");
            if (!Files.exists(copyLocation)) {
                Files.createDirectories(copyLocation);
            }

            // Save the file using the `location` string as the filename
            Path savePath = copyLocation.resolve(location);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, savePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Could not store file: " + location, e);
            }
        }

        return formDocument;
    }

    @Transactional
    public void deleteRecord(Long id) {
        formRepository.deleteById(id);
    }

    public List<FormDTO> getAllListRecords() {
        return formRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<FormModel> findAll() {
        return formRepository.findAll();
    }

    public FormModel findById(Long id) {
        return formRepository.findById(id).orElse(null);
    }

    public FormModel findNext(Long id) {
        Optional<FormModel> nextForm = formRepository.findFirstByIdGreaterThanOrderByIdAsc(id);
        return nextForm.orElse(null);
    }

    public FormModel findPrevious(Long id) {
        Optional<FormModel> previousForm = formRepository.findFirstByIdLessThanOrderByIdDesc(id);
        return previousForm.orElse(null);
    }

    public FormModel findFirst() {
        Optional<FormModel> firstForm = formRepository.findFirstByOrderByIdAsc();
        return firstForm.orElse(null);
    }

    public FormModel findLast() {
        Optional<FormModel> lastForm = formRepository.findFirstByOrderByIdDesc();
        return lastForm.orElse(null);
    }

    private FormDTO mapToDTO(FormModel form) {
        FormDTO dto = new FormDTO();
        dto.setId(form.getId());
        dto.setName(form.getName());
        dto.setDescription(form.getDescription());
        dto.setLocation(form.getLocation());
        //System.out.println(dto);
        return dto;
    }
}

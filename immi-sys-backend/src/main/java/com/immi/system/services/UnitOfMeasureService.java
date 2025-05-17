package com.immi.system.services;

import com.immi.system.models.UnitOfMeasureModel;
import com.immi.system.repositories.UnitOfMeasureRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepo;

    public UnitOfMeasureService(UnitOfMeasureRepository unitOfMeasureRepo) {
        this.unitOfMeasureRepo = unitOfMeasureRepo;
    }

    public UnitOfMeasureModel createUnitOfMeasure(UnitOfMeasureModel unit) {
        if (unitOfMeasureRepo.findByName(unit.getName()) != null) {
            throw new RuntimeException("Unit of Measure already exists");
        }

        return unitOfMeasureRepo.save(unit);
    }

    public List<UnitOfMeasureModel> getAllUnitsOfMeasure() {
        return unitOfMeasureRepo.findAll();
    }

    public UnitOfMeasureModel getUnitOfMeasureById(Long id) {
        return unitOfMeasureRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Unit of Measure not found"));
    }

    public UnitOfMeasureModel getNextUnitOfMeasure(Long id) {
        return unitOfMeasureRepo.findFirstByIdGreaterThanOrderByIdAsc(id);
    }

    public UnitOfMeasureModel getPreviousUnitOfMeasure(Long id) {
        return unitOfMeasureRepo.findFirstByIdLessThanOrderByIdDesc(id);
    }

    public UnitOfMeasureModel getFirstUnitOfMeasure() {
        return unitOfMeasureRepo.findFirstByOrderByIdAsc();
    }

    public UnitOfMeasureModel getLastUnitOfMeasure() {
        return unitOfMeasureRepo.findFirstByOrderByIdDesc();
    }

    public UnitOfMeasureModel updateUnitOfMeasure(Long id, UnitOfMeasureModel unit) {
        UnitOfMeasureModel existingUnit = unitOfMeasureRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Unit of Measure not found"));

        existingUnit.setName(unit.getName());
        existingUnit.setDescription(unit.getDescription());

        return unitOfMeasureRepo.save(existingUnit);
    }

    public void deleteUnitOfMeasure(Long id) {
        unitOfMeasureRepo.deleteById(id);
    }
}

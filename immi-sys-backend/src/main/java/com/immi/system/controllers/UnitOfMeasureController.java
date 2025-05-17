package com.immi.system.controllers;

import com.immi.system.models.UnitOfMeasureModel;
import com.immi.system.services.UnitOfMeasureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/common/units-of-measure")
public class UnitOfMeasureController {

    private final UnitOfMeasureService unitOfMeasureService;

    public UnitOfMeasureController(UnitOfMeasureService unitOfMeasureService) {
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<UnitOfMeasureModel>> getAllUnitsOfMeasure() {
        List<UnitOfMeasureModel> units = unitOfMeasureService.getAllUnitsOfMeasure();
        return ResponseEntity.ok(units);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitOfMeasureModel> getUnitOfMeasureById(@PathVariable Long id) {
        UnitOfMeasureModel unit = unitOfMeasureService.getUnitOfMeasureById(id);
        return ResponseEntity.ok(unit);
    }

    @PostMapping
    public ResponseEntity<UnitOfMeasureModel> createUnitOfMeasure(@RequestBody UnitOfMeasureModel unit) {
        UnitOfMeasureModel createdUnit = unitOfMeasureService.createUnitOfMeasure(unit);
        return new ResponseEntity<>(createdUnit, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnitOfMeasureModel> updateUnitOfMeasure(@PathVariable Long id, @RequestBody UnitOfMeasureModel unit) {
        UnitOfMeasureModel updatedUnit = unitOfMeasureService.updateUnitOfMeasure(id, unit);
        return ResponseEntity.ok(updatedUnit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnitOfMeasure(@PathVariable Long id) {
        unitOfMeasureService.deleteUnitOfMeasure(id);
        return ResponseEntity.noContent().build();
    }
}

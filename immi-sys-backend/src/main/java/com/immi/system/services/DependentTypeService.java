package com.immi.system.services;

import com.immi.system.models.DependentTypeModel;
import com.immi.system.repositories.DependentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DependentTypeService {

    @Autowired
    public DependentTypeRepository dependentTypeRepository;
    
    public DependentTypeModel findById(Long dependentTypeId) {
        return dependentTypeRepository.findById(dependentTypeId).orElse(null);
    }
    
}

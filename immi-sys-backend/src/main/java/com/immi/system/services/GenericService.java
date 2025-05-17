package com.immi.system.services;

import com.immi.system.models.CategoryModel;
import com.immi.system.models.CountryModel;
import com.immi.system.models.StatusModel;
import com.immi.system.models.VisaTypeModel;
import com.immi.system.repositories.CategoryRepository;
import com.immi.system.repositories.CountryRepository;
import com.immi.system.repositories.StatusRepository;
import com.immi.system.repositories.VisaTypeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenericService {
    
    // Process function for Category
    @Autowired
    private CategoryRepository categoryRepository;
    public List<CategoryModel> getAllCategories() {
        return categoryRepository.findAll();
    }
    public void saveCategory(CategoryModel category) {
        categoryRepository.save(category);
    }
    public CategoryModel findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }
    
    // Process function for VisaTypeModel
    @Autowired
    private VisaTypeRepository visaTypeRepository;
    public List<VisaTypeModel> getAllVisaTypes() {
        return visaTypeRepository.findAll();
    }
    public VisaTypeModel findVisaTypeByName(String name) {
        return visaTypeRepository.findByName(name);
    }
    public void saveVisaType(VisaTypeModel visaType) {
        visaTypeRepository.save(visaType);
    }
    
    // Process function for COUNTRYMODEL
    @Autowired
    private CountryRepository countryRepository;
    public List<CountryModel> getAllCountries() {
        return countryRepository.findAll();
    }
    public CountryModel findCountryByName(String name) {
        return countryRepository.findByName(name);
    }
    public void saveCountry(CountryModel country) {
        countryRepository.save(country);
    }

    // Process function for StatusModel
    @Autowired
    private StatusRepository statusRepository;
    public List<StatusModel> getAllStatuses() {
        return statusRepository.findAll();
    }
    public StatusModel findStatusByName(String name) {
        return statusRepository.findByName(name);
    }
    public void saveStatus(StatusModel status) {
        statusRepository.save(status);
    }
    
}

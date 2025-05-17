package com.immi.system.services;

import com.immi.system.models.ApplicationEventModel;
import com.immi.system.repositories.ProcessEventRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProcessEventService {

    private final ProcessEventRepository processEventRepository;
    
    public ProcessEventService(ProcessEventRepository processEventRepository) {
        this.processEventRepository = processEventRepository;
    }
    
    public List<ApplicationEventModel> getEventsForProcess(String sourceType, Long processId) {
        return processEventRepository.findBySourceTypeAndProcessId(sourceType, processId);
    }

    public ApplicationEventModel addEvent(Long processId, ApplicationEventModel event) {
        return processEventRepository.save(event);
    }

    // DELETE EVENT PROCESS
    public boolean existEventById(Long id) {
        return processEventRepository.existsById(id);
    }
    public void deleteEventById(Long id) {
        processEventRepository.deleteById(id);
    }
    
}

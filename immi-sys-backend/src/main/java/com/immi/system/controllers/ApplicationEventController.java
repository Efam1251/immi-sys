package com.immi.system.controllers;

import com.immi.system.models.ApplicationEventModel;
import com.immi.system.services.ProcessEventService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/immigration/application-event")
public class ApplicationEventController {
    
    private final ProcessEventService processEventService;
    
    public ApplicationEventController(ProcessEventService processEventService){
        this.processEventService = processEventService;
    }
    
    @GetMapping("/events")
    public List<ApplicationEventModel> getEventsByPetition(
            @RequestParam("sourceType") String sourceType,
            @RequestParam("id") Long processId) {
        return processEventService.getEventsForProcess(sourceType, processId);
    }

    @PostMapping("/event-save")
    public ResponseEntity<ApplicationEventModel> addEvent(
            @RequestParam("sourceType") String sourceType,
            @RequestParam("id") Long processId,
            @Valid @RequestBody ApplicationEventModel event) {

        event.setSourceType(sourceType);
        System.out.println("Event: " + event.toString());
        ApplicationEventModel createdEvent = processEventService.addEvent(processId, event);
        
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @DeleteMapping("/event-delete")
    public ResponseEntity<Void> deleteEvent(
            @RequestParam("id") Long id) {
        
        if (!processEventService.existEventById(id)) {
            return ResponseEntity.notFound().build();
        }
        processEventService.deleteEventById(id);
        return ResponseEntity.noContent().build();
    }
}

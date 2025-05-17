package com.immi.system.controllers;

import com.immi.system.DTOs.StateDTO;
import com.immi.system.services.StateService;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/common/location/states")
public class StateController {

    private static final Logger logger = LoggerFactory.getLogger(StateController.class);
    
    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }
    
    @GetMapping
    public ResponseEntity<?> getRequestedState(
            @RequestParam("id") String id,
            @RequestParam(required = false) String direction) {
        
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "ID parameter is required"));
        }
        Long currentId;
        try {
            currentId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid ID format"));
        }
        StateDTO state = switch (direction) {
            case "Current" -> stateService.getStateById(currentId);
            case "Next" -> stateService.getNextState(currentId);
            case "Previous" -> stateService.getPreviousState(currentId);
            case "First" -> stateService.getFirstState();
            case "Last" -> stateService.getLastState();
            default -> null;
        };
        if (state == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "State not found"));
        }

        return ResponseEntity.ok(state);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<StateDTO>> getAllStates() {
        List<StateDTO> states = stateService.getAllStates();
        return new ResponseEntity<>(states, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StateDTO> getStateById(@PathVariable Long id) {
        StateDTO state = stateService.getStateById(id);
        if (state == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(state, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<?> createState(@RequestBody StateDTO dto) {
        logger.info("Creating a new state: {}", dto);
        try {
            StateDTO createdState = stateService.createState(dto);
            return new ResponseEntity<>(createdState, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StateDTO> updateState(@PathVariable Long id, @RequestBody StateDTO dto) {
        logger.info("Updating state with ID: {}", id);
        StateDTO updatedState = stateService.updateState(id, dto);
        if (updatedState == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedState, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Long id) {
        logger.warn("Deleting state with ID: {}", id);
        stateService.deleteState(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

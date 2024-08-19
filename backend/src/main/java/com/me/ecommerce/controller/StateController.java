package com.me.ecommerce.controller;

import com.me.ecommerce.entity.State;
import com.me.ecommerce.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value ="/api/states", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200") // Replace with your frontend URL
public class StateController {

    private final StateService stateService;

    @Autowired
    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping
    public List<State> getAllStates() {
        return stateService.getAllStates();
    }

    @GetMapping("/{id}")
    public ResponseEntity<State> getStateById(@PathVariable Long id) {
        Optional<State> state = stateService.getStateById(id);
        return state.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public State createState(@RequestBody State state) {
        return stateService.saveState(state);
    }

    @PutMapping("/{id}")
    public ResponseEntity<State> updateState(@PathVariable Long id, @RequestBody State stateDetails) {
        Optional<State> optionalState = stateService.getStateById(id);
        if (optionalState.isPresent()) {
            State state = optionalState.get();
            state.setName(stateDetails.getName());
            return ResponseEntity.ok(stateService.saveState(state));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Long id) {
        stateService.deleteState(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/country/{code}")
    public ResponseEntity<List<State>> getStatesByCountryCode(@PathVariable String code) {
        List<State> states = stateService.getStatesByCountryCode(code);
        if (states.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(states);
        }
    }
}

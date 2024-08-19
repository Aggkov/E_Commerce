package com.me.ecommerce.controller;

import com.me.ecommerce.dto.response.ApiResponse;
import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.StateDTO;
import com.me.ecommerce.entity.State;
import com.me.ecommerce.service.StateService;
import com.me.ecommerce.utils.AppConstants;
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
    public PagedResponse<StateDTO> getAllStates(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        return stateService.getAllStates(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StateDTO> getStateById(@PathVariable Long id) {
        return ResponseEntity.ok(stateService.getStateById(id));
    }

    @PostMapping
    public ResponseEntity<State> createState(@RequestBody State state) {
        return ResponseEntity.ok(stateService.saveState(state));
    }

    @PutMapping("/{id}")
    public ResponseEntity<State> updateState(@PathVariable Long id, @RequestBody State state) {
        return ResponseEntity.ok(stateService.updateState(id, state));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse> deleteState(@PathVariable Long id) {
//        return stateService.deleteState(id);
//    }

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

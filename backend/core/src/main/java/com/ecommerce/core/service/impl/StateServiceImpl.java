package com.ecommerce.core.service.impl;

import com.ecommerce.core.dto.response.StateDTO;
import com.ecommerce.core.entity.State;
import com.ecommerce.core.mapper.StateMapper;
import com.ecommerce.core.repository.StateRepository;
import com.ecommerce.core.service.StateService;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;
    private final StateMapper stateMapper;

    @Autowired
    public StateServiceImpl(StateRepository stateRepository, StateMapper stateMapper) {
        this.stateRepository = stateRepository;
        this.stateMapper = stateMapper;
    }

    public List<StateDTO> getAllStates() {

        List<State> states = stateRepository.findAll();

        if (states.isEmpty()) {
            throw new RuntimeException("States are empty");
        }

        return states.stream()
                .map(stateMapper::stateToStateDTO)
                .toList();
    }

    public StateDTO getStateById(UUID id) {
        return stateRepository.findById(id)
                .map(stateMapper::stateToStateDTO)
                .orElseThrow(() -> new RuntimeException("State not found"));
    }

    public State saveState(State state) {
        return stateRepository.save(state);
    }

    public void deleteState(UUID id) {
        stateRepository.deleteById(id);
    }

    public List<StateDTO> getStatesByCountryCode(String code) {
        List<State> states = stateRepository.findStatesByCountryCodeOrderById(code);
        if (states.isEmpty()) {
            throw new RuntimeException("States are empty");
        } else {
            return states.stream()
                    .map(stateMapper::stateToStateDTO)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public State updateState(UUID id, State state) {
        State existingState = stateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("State not found"));

        // Update the existing product with new values
        existingState.setName(state.getName());
        existingState.setCountry(state.getCountry());
        // Update other fields as necessary

        // Save and return the updated product
        return stateRepository.save(existingState);
    }
}

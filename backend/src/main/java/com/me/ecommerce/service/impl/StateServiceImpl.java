package com.me.ecommerce.service.impl;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.dto.response.StateDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.entity.State;
import com.me.ecommerce.mapper.StateMapper;
import com.me.ecommerce.repository.StateRepository;
import com.me.ecommerce.service.StateService;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


import static com.me.ecommerce.utils.AppConstants.CREATED_AT;

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

    public StateDTO getStateById(Long id) {
        return stateRepository.findById(id)
                .map(stateMapper::stateToStateDTO)
                .orElseThrow(() -> new RuntimeException("State not found"));
    }

    public State saveState(State state) {
        return stateRepository.save(state);
    }

    public void deleteState(Long id) {
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
    public State updateState(Long id, State state) {
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

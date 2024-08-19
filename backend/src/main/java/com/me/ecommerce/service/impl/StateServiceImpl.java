package com.me.ecommerce.service.impl;

import com.me.ecommerce.entity.State;
import com.me.ecommerce.repository.StateRepository;
import com.me.ecommerce.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    @Autowired
    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public List<State> getAllStates() {
        return stateRepository.findAll();
    }

    public Optional<State> getStateById(Long id) {
        return stateRepository.findById(id);
    }

    public State saveState(State state) {
        return stateRepository.save(state);
    }

    public void deleteState(Long id) {
        stateRepository.deleteById(id);
    }

    public List<State> getStatesByCountryCode(String code) {
        return stateRepository.findStatesByCountryCode(code);
    }
}

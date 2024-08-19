package com.me.ecommerce.service;

import com.me.ecommerce.entity.State;
import java.util.List;
import java.util.Optional;

public interface StateService {

    List<State> getAllStates();

    Optional<State> getStateById(Long id);

    State saveState(State state);

    void deleteState(Long id);

    List<State> getStatesByCountryCode(String code);
}

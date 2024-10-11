package com.ecommerce.core.service;

import com.ecommerce.core.dto.response.StateDTO;
import com.ecommerce.core.entity.State;
import java.util.List;
import java.util.UUID;

public interface StateService {

    List<StateDTO> getAllStates();

    StateDTO getStateById(UUID id);

    State saveState(State state);

    void deleteState(UUID id);

    List<StateDTO> getStatesByCountryCode(String code);

    State updateState(UUID id, State state);
}

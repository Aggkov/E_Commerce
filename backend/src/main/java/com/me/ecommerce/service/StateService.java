package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.StateDTO;
import com.me.ecommerce.entity.State;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface StateService {

    List<StateDTO> getAllStates();

    StateDTO getStateById(UUID id);

    State saveState(State state);

    void deleteState(UUID id);

    List<StateDTO> getStatesByCountryCode(String code);

    State updateState(UUID id, State state);
}

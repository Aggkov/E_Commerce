package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.StateDTO;
import com.me.ecommerce.entity.State;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

public interface StateService {

    PagedResponse<StateDTO> getAllStates(Integer page, Integer size);

    StateDTO getStateById(Long id);

    State saveState(State state);

    void deleteState(Long id);

    List<StateDTO> getStatesByCountryCode(String code);

    State updateState(Long id, State state);
}

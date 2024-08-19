package com.me.ecommerce.mapper;

import com.me.ecommerce.dto.response.StateDTO;
import com.me.ecommerce.entity.State;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StateMapper {

    StateMapper INSTANCE = Mappers.getMapper(StateMapper.class);

    // Simple mapping method
    StateDTO stateToStateDTO(State state);
    // Reverse mapping method
    State stateDTOToState(StateDTO stateDTO);
}

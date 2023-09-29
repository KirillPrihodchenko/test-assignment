package com.testAssignment.demo.dto;

import com.testAssignment.demo.model.Users;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class MappingUpdateUserDTO {

    private final ModelMapper modelMapper;

    public MappingUpdateUserDTO(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Users convertToEntity(RequestUpdateUserDTO requestUpdateUserDTO) {
        return modelMapper.map(requestUpdateUserDTO, Users.class);
    }
}
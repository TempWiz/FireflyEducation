package com.example.demo.converter;
import com.example.demo.dto.requestDTO.UserRequest;
import com.example.demo.dto.responsDTO.UserSignUpResponse;
import com.example.demo.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    @Autowired
    private ModelMapper modelMapper;
    public UserEntity convertToEntity(UserRequest userRequest) {
        return modelMapper.map(userRequest, UserEntity.class);
    }
    public UserSignUpResponse.Data convertToResponse(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserSignUpResponse.Data.class);
    }
}

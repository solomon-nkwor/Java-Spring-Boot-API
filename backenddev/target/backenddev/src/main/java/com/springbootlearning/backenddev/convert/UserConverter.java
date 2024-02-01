package com.springbootlearning.backenddev.convert;

import com.springbootlearning.backenddev.DTO.UserDTO;
import com.springbootlearning.backenddev.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    private ModelMapper modelMapper;
    public UserConverter(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }
    public UserDTO convertUsertoDTO(User user){
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
        return userDTO;
    }
    public User convertUserDTOtoUser(UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);
        return user;
    }
}


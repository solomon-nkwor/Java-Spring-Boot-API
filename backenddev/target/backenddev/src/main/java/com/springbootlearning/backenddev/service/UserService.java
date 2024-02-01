package com.springbootlearning.backenddev.service;

import com.springbootlearning.backenddev.DTO.NewUserRequestDTO;
import com.springbootlearning.backenddev.DTO.UpdatedUserDTO;
import com.springbootlearning.backenddev.DTO.UserDTO;
import com.springbootlearning.backenddev.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<UserDTO> getAllUsers();
    public UserDTO getUserByID(String id);
    public UserDTO getUserByEmail(String email);
    public UserDTO createUser(NewUserRequestDTO newUser);
    public UserDTO updateUser(String id, UpdatedUserDTO updatedUserDTO);
    public UserDTO deleteUserByID(String id);
}

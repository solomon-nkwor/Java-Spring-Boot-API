package com.springbootlearning.backenddev.service;

import com.springbootlearning.backenddev.DTO.NewUserRequestDTO;
import com.springbootlearning.backenddev.DTO.UpdatedUserDTO;
import com.springbootlearning.backenddev.DTO.UserDTO;
import com.springbootlearning.backenddev.Repository.UserRepository;
import com.springbootlearning.backenddev.convert.UserConverter;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.springbootlearning.backenddev.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserConverter userConverter;
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter){
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }
    public List<UserDTO> getAllUsers() {
        log.info("getAllUsers Started");
        List<User> allUsers = userRepository.findAll();
        List<UserDTO>  userDTOs = allUsers.stream().map(user -> userConverter.convertUsertoDTO(user))
                .collect(Collectors.toList());
        log.info("getAllUsers Completed");
        return userDTOs;
    }

    public UserDTO getUserByID(String id) {
        log.info("getUserByID Started");
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            log.info("getUserByID completed");
            return userConverter.convertUsertoDTO(user);
        }
        else{
            return null;
        }
    }

    public UserDTO getUserByEmail(String email) {
        log.info("getUserByEmail Started");
        User user = userRepository.findByEmail(email);
        if(user != null){
            log.info("getUserByEmail completed");
            return userConverter.convertUsertoDTO(user);
        }
        else{
            return null;
        }
    }

    @Override
    public UserDTO createUser(NewUserRequestDTO newUserRequestDTO) {
        log.info("CreateUser Started");
        if (userRepository.existsByEmail(newUserRequestDTO.getEmail())) {
            throw new ConstraintViolationException("User with this email already exists", null);
//        } else if (userRepository.existsByFirstname(newUserRequestDTO.getFirstName())) {
//            throw new ConstraintViolationException("User with this firstname already exist", null);
//
        }
        User newUser = new User();

        newUser.setEmail(newUserRequestDTO.getEmail());
        newUser.setFirstName(newUserRequestDTO.getFirstName());
        newUser.setLastName(newUserRequestDTO.getLastName());
        newUser.setPassword(newUserRequestDTO.getPassword());
        log.info("Create user before save id: " + newUser.getId());
        newUser = userRepository.save(newUser);
        log.info("Create user after save id: " + newUser.getId());

        return userConverter.convertUsertoDTO(newUser);
    }


    public UserDTO updateUser(String id, UpdatedUserDTO updatedUserDTO) {
        log.info("CreateUser Started");
        User updatedUser = userRepository.findById(id).orElse(null);

        if(updatedUser == null){
            return null;
        }

        updatedUser.setEmail(updatedUserDTO.getEmail());
        updatedUser.setFirstName(updatedUserDTO.getFirstName());
        updatedUser.setLastName(updatedUserDTO.getLastName());
        updatedUser = userRepository.save(updatedUser);
        log.info("Create user after save id: " + updatedUser.getId());

        return userConverter.convertUsertoDTO(updatedUser);
    }


    public UserDTO deleteUserByID(String id) {
        log.info("CreateUser Started");
        User deletedUser = userRepository.findById(id).orElse(null);
        if (deletedUser == null){
            return null;
        }
        userRepository.deleteById(id);


        return userConverter.convertUsertoDTO(deletedUser);
    }
}

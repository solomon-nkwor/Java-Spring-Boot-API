package com.springbootlearning.backenddev.controller;

import com.springbootlearning.backenddev.DTO.NewUserRequestDTO;
import com.springbootlearning.backenddev.DTO.UpdatedUserDTO;
import com.springbootlearning.backenddev.DTO.UserDTO;
import com.springbootlearning.backenddev.model.User;
import com.springbootlearning.backenddev.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "Users API")
public class UserRestController {
    @Value("${apiKey}")
    private String API_KEY;

    //    If you have more than one api keys available, the variable would be declared as below
//    private List<String> API_KEYS;

    private UserService userService;

    private static final String BAD_API_KEY = "{\"status\":\"Authorization Failed\", \"message\":\"Invalid API Key\"}";

    public UserRestController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/users")
    @Operation(summary = "Retrieve All Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found one or more users",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))) }),
            @ApiResponse(responseCode = "403", description = "Authorization Failed", content = @Content)})

    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "apiKey", required = false) String apiKey){

        if(apiKey == null || !apiKey.equals(API_KEY)){
            log.error("Invalid API Key");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_API_KEY);
        }
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Retrieves single user based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user matching this ID",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "403", description = "Authorization Failed", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<?> getUsersById(@RequestHeader(value = "apiKey", required = false) String apiKey,
                                          @Parameter(description = "ID of user to be found") @PathVariable String id){

        // In the case of more than one API Key, our if statement would look like this
//        if (apikey == null || !apiKey.contains(apikey)
//          ...... continue with the logic.

        if(apiKey == null || !apiKey.equals(API_KEY)){
            log.error("Invalid API Key");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_API_KEY);
        }

        UserDTO user = userService.getUserByID(id);
        if (user != null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{}");
        }

    }

    @GetMapping("/users/userByEmail/{email}")
    @Operation(summary = "Retrieves users by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user matching this email",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "403", description = "Authorization Failed", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<?> getUsersByEmail(@RequestHeader(value = "apiKey", required = false) String apiKey,
                                             @Parameter(description = "ID of user to be found") @PathVariable String email){

        if(apiKey == null || !apiKey.equals(API_KEY)){
            log.error("Invalid API Key");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_API_KEY);
        }

        UserDTO user = userService.getUserByEmail(email);
        if (user != null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{}");
        }

    }
    @PostMapping("/users")
    @Operation(summary = "Registers new users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully Created new User",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "403", description = "Authorization Failed", content = @Content),@ApiResponse(responseCode = "403", description = "Authorization Failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Failed to create new user", content = @Content)
    })
    public ResponseEntity<?> createUser(@RequestHeader(value = "apiKey", required = false)
                                            String apiKey, @Parameter(description = "New user Body Content to be Created")
                                        @Valid @RequestBody NewUserRequestDTO newUserRequest){
        if(apiKey == null || !apiKey.equals(API_KEY)){
            log.error("Invalid API Key");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_API_KEY);
        }

        try{
            log.info("new user request: " + newUserRequest);

        UserDTO userDTO = userService.createUser(newUserRequest);

        log.info("create user completed!");
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        }
        catch(ConstraintViolationException cve) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this email already exist, please enter another email");
        }

    }
    @PutMapping("/users/{id}")
    @Operation(summary = "Updates single user based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated user information matching this ID",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "403", description = "Authorization Failed", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<?> updateUser(@RequestHeader(value = "apiKey", required = false) String apiKey, @PathVariable String id, @Valid @RequestBody UpdatedUserDTO updatedUserDTO){
        // find a user DTO that matches the id
        if(apiKey == null || !apiKey.equals(API_KEY)){
            log.error("Invalid API Key");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_API_KEY);
        }
        UserDTO updatedUser = userService.updateUser(id, updatedUserDTO);
        if (updatedUser == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{}");
        }
        log.info("update User completed");
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);

    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Deletes single user based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the user matching this ID",
                    content = {@Content(mediaType = "application/json", schema = @Schema($schema = "{}")) }),
            @ApiResponse(responseCode = "403", description = "Authorization Failed", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<?> deleteUser(@RequestHeader(value = "apiKey", required = false) String apiKey, @PathVariable String id){
        if(apiKey == null || !apiKey.equals(API_KEY)){
            log.error("Invalid API Key");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_API_KEY);
        }

        log.info("Delete user started");
        UserDTO deletedUser = userService.deleteUserByID(id);
        if (deletedUser == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{}");
        }
        log.info("Delete user complete");
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler (MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getConstraintViolations().forEach(cv -> {
//            String fieldName = cv.getPropertyPath().toString();
//            String errorMessage = cv.getMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
//    }

}

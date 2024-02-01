package com.springbootlearning.backenddev.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatedUserDTO {
    @Valid

    @NotNull(message = "email is required")
    @NotBlank(message = "email is required")
    private String email;

    @NotNull(message = "firstname is required")
    @NotBlank(message = "firstname is required")
    private String firstName;

    @NotNull(message = "lastname is required")
    @NotBlank(message = "lastname is required")
    @Size(min = 2, max = 200, message = "lastName must be between 2 and 200 characters")
    private String lastName;

}

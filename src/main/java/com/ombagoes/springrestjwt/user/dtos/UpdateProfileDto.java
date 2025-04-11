package com.ombagoes.springrestjwt.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateProfileDto {
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "[a-zA-Z\\s]+$", message = "invalid name, char only")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;
}

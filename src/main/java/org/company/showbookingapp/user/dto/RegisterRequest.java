package org.company.showbookingapp.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    private String password;
    private String confirmPassword;
}

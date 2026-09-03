package org.company.showbookingapp.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateManagerRequest {

    @NotBlank(message = "Manager username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @NotBlank(message = "Manager email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Manager password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

}

package org.company.showbookingapp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.company.showbookingapp.user.Role;

@Getter
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;

}

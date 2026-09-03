package org.company.showbookingapp.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.user.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody RegisterRequest request)
    {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest request)
    {
        return userService.loginUser(request);
    }

    @PostMapping("/venue-managers")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO createVenueManager(@Valid @RequestBody CreateManagerRequest request)
    {
        return userService.createVenueManager(request);
    }
}

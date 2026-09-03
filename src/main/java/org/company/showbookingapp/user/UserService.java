package org.company.showbookingapp.user;

import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.jwt.JwtService;
import org.company.showbookingapp.security.*;
import org.company.showbookingapp.user.dto.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User registerUser(RegisterRequest request)
    {
        if(!request.getPassword().equals(request.getConfirmPassword()))
        {
            throw new RuntimeException("Password do not match");
        }

        if(userRepository.existsByUsername(request.getUsername()))
        {
            throw new RuntimeException("Username already exists");
        }

        if(userRepository.existsByEmail(request.getEmail()))
        {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public LoginResponse loginUser(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(),request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByUsername(request.getLogin());

        User user = userDetails.getUser();

        String accessToken = jwtService.generateToken(userDetails);
        return new LoginResponse(accessToken, user.getId(),user.getUsername(),user.getEmail(),user.getRole());
    }

    public UserResponseDTO createVenueManager(CreateManagerRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User manager = new User();

        manager.setUsername(request.getUsername());
        manager.setEmail(request.getEmail());
        manager.setPassword(passwordEncoder.encode(request.getPassword()));
        manager.setRole(Role.VENUE_MANAGER);

        User savedManager = userRepository.save(manager);

        return new UserResponseDTO(savedManager.getId(), savedManager.getUsername(), savedManager.getEmail(), savedManager.getRole());

    }
}



package org.company.showbookingapp.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security-test")
public class SecurityTestController {
    @GetMapping("/me")
    public String getCurrentUser(Authentication authentication) {

        return "Authenticated user: "
                + authentication.getName();
    }

    @GetMapping("/admin")

    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {

        return "Welcome ADMIN";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userOnly() {

        return "Welcome USER";
    }

}

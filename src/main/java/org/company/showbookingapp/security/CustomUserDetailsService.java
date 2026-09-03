package org.company.showbookingapp.security;

import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.user.User;
import org.company.showbookingapp.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(login).orElseGet(() -> userRepository.findByEmail(login).orElseThrow(() -> new UsernameNotFoundException("User not found")));

        return new CustomUserDetails(user);
    }

}

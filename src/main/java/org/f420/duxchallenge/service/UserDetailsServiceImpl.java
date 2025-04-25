package org.f420.duxchallenge.service;

import org.f420.duxchallenge.dao.LoginRequestDTO;
import org.f420.duxchallenge.dao.UserRepository;
import org.f420.duxchallenge.dto.AuthResponse;
import org.f420.duxchallenge.exceptions.ApiException;
import org.f420.duxchallenge.utils.JWTUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.f420.duxchallenge.enums.ErrorMessage.BAD_CREDENTIALS;
import static org.f420.duxchallenge.enums.ErrorMessage.USERNAME_NOT_FOUND;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(USERNAME_NOT_FOUND, username));
    }


}

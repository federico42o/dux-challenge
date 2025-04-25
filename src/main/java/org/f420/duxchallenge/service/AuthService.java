package org.f420.duxchallenge.service;

import lombok.extern.slf4j.Slf4j;
import org.f420.duxchallenge.dao.LoginRequestDTO;
import org.f420.duxchallenge.dto.AuthResponse;
import org.f420.duxchallenge.exceptions.ApiException;
import org.f420.duxchallenge.utils.JWTUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.f420.duxchallenge.enums.ErrorMessage.BAD_CREDENTIALS;

@Service
@Slf4j
public class AuthService {

    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthService(JWTUtils jwtUtils, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }


    public AuthResponse loginUser(LoginRequestDTO loginRequest){
        Authentication authentication = this.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.createToken(authentication);
        return new AuthResponse(token);
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null){
            throw new ApiException(BAD_CREDENTIALS);
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new ApiException(BAD_CREDENTIALS);
        }
        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());

    }

    public String getEncryptedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}

package org.f420.duxchallenge.resource;

import jakarta.validation.Valid;
import org.f420.duxchallenge.dao.LoginRequestDTO;
import org.f420.duxchallenge.dto.AuthResponse;
import org.f420.duxchallenge.service.AuthService;
import org.f420.duxchallenge.service.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthResource {
    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/getPassword")
    public String registerUser(@RequestBody @Valid LoginRequestDTO userDtoReq){
        return authService.getEncryptedPassword(userDtoReq.getPassword());
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        return new ResponseEntity<>(authService.loginUser(loginRequest), HttpStatus.OK);
    }
}

package org.f420.duxchallenge.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.f420.duxchallenge.exceptions.ApiException;
import org.f420.duxchallenge.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import static org.f420.duxchallenge.constants.Constants.CLAIM_AUTHORITIES;
import static org.f420.duxchallenge.constants.Constants.CLAIM_USERNAME;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(username = "admin", authorities = { "admin" })
@Slf4j
public class JWTUtilsTest {

    @Autowired
    private JWTUtils jwtUtils;

    @BeforeEach
    void setUp() {

    }

    @Test
    void shouldCreateValidToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtUtils.createToken(authentication);
        assertNotNull(token);
    }


    @Test
    void validateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtUtils.createToken(authentication);
        DecodedJWT decodedJWT = jwtUtils.validateToken(token);
        assertNotNull(decodedJWT);
    }

    @Test
    void shouldNotValidateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        log.info("username: {}", username);
        ZonedDateTime nowTime = ZonedDateTime.now();
        ZonedDateTime yesterday = ZonedDateTime.now().minusDays(1);
        String token = JWT.create()
                .withIssuer(jwtUtils.getIssuer())
                .withClaim(CLAIM_USERNAME, username)
                .withClaim(CLAIM_AUTHORITIES, "admin")
                .withIssuedAt(Date.from(nowTime.toInstant()))
                .withExpiresAt(Date.from(yesterday.toInstant()))
                .withNotBefore(Date.from(nowTime.toInstant()))
                .withJWTId(UUID.randomUUID().toString())
                .sign(jwtUtils.getSignAlgorithm());
        assertThrows(ApiException.class, () -> jwtUtils.validateToken(token));
    }

    @Test
    void extractUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String expected = ((UserDetails) authentication.getPrincipal()).getUsername();
        String token = JWT.create()
                .withIssuer(jwtUtils.getIssuer())
                .withClaim(CLAIM_USERNAME, expected)
                .sign(jwtUtils.getSignAlgorithm());
        DecodedJWT decodedJWT = jwtUtils.validateToken(token);
        assertNotNull(expected);
        String username = jwtUtils.extractUsername(decodedJWT);
        assertNotNull(username);
        assertEquals(expected, username);
        log.info("expected: {}, extracted username:{}", expected, username);
    }

}

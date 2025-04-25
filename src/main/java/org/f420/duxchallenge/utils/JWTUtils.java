package org.f420.duxchallenge.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.f420.duxchallenge.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.f420.duxchallenge.constants.Constants.*;
import static org.f420.duxchallenge.enums.ErrorMessage.MALFORMED_JWT;

@Component
@Getter
@Slf4j
public class JWTUtils {

    @Value("${spring.jwt.private.key}")
    private String privateKey;

    @Value("${spring.application.name}")
    private  String issuer;

    private Algorithm signAlgorithm;

    @PostConstruct
    public void basicConfigurations() {
        this.signAlgorithm = Algorithm.HMAC256(this.privateKey);
    }

    /**
     * Crear un token jwt
     *
     * @param authentication datos de usuario autenticado
     * @return String de JWT token
     */
    public String createToken(Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        ZonedDateTime nowTime = ZonedDateTime.now();
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        try {
            return JWT.create()
                    .withIssuer(this.issuer)
                    .withClaim(CLAIM_USERNAME, username)
                    .withClaim(CLAIM_AUTHORITIES, authorities)
                    .withIssuedAt(Date.from(nowTime.toInstant()))
                    .withExpiresAt(Date.from(nowTime.plusSeconds(TOKEN_LIFETIME).toInstant()))
                    .withNotBefore(Date.from(nowTime.toInstant()))
                    .withJWTId(UUID.randomUUID().toString())
                    .sign(signAlgorithm);
        } catch (JWTCreationException | IllegalArgumentException ex) {
            log.error("Error creando token");
            log.error(ex.getMessage(),  ex);
            return null;
        }
    }

    /**
     * Decodifica y valida el jwt token
     *
     * @param token jwt token
     * @return DecodedJWT
     * @throws JWTVerificationException token inválido
     */
    public DecodedJWT validateToken(String token) {

        try {
            JWTVerifier verifier = JWT.require(signAlgorithm)
                    .withIssuer(this.issuer)
                    .acceptExpiresAt(0)
                    .build();

            return verifier.verify(token);

        } catch (JWTVerificationException ex) {
            log.error("Error verificando token");
            log.error(ex.getMessage(),  ex);
            throw new ApiException(MALFORMED_JWT);
        }
    }

    /**
     * Extrae el username del jwt
     *
     * @param decodedJWT jwt decodificado
     * @return String username
     */
    public String extractUsername(DecodedJWT decodedJWT) {
        return Optional.ofNullable(decodedJWT.getClaim(CLAIM_USERNAME)).map(Claim::asString).orElse(null);
    }

    /**
     * Ver un claim especifico
     *
     * @param decodedJWT jwt decodificado
     * @param claimName  nombre de claim
     * @return Claim selecionado
     */
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

}

package org.f420.duxchallenge.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.f420.duxchallenge.dto.ErrorResponse;
import org.f420.duxchallenge.enums.ErrorMessage;
import org.f420.duxchallenge.exceptions.ApiException;
import org.f420.duxchallenge.utils.JWTUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collection;

import static org.f420.duxchallenge.constants.Constants.CLAIM_AUTHORITIES;
import static org.f420.duxchallenge.constants.Constants.JSON_CONTENT_TYPE;
import static org.f420.duxchallenge.utils.ConvertUtils.toObjectString;

@Slf4j
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;

    public JwtTokenValidatorFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jwtToken != null){
            try {
                jwtToken = jwtToken.substring(7);
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
                String username = jwtUtils.extractUsername(decodedJWT);
                String authorities = jwtUtils.getSpecificClaim(decodedJWT, CLAIM_AUTHORITIES).asString();
                Collection<? extends GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authoritiesList);
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            } catch (ApiException ex) {
                buildResponse(response);
                return;
            }
        }
         filterChain.doFilter(request, response);
    }

    private void buildResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(JSON_CONTENT_TYPE);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(ErrorMessage.MALFORMED_JWT.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timestamp((ZonedDateTime.now()))
                .build();
        response.getWriter().write(toObjectString(errorResponse));
    }
}

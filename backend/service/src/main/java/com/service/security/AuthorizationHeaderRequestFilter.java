package com.service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * Filter for handling header parsing.
 */
@Component
@Slf4j
public class AuthorizationHeaderRequestFilter extends OncePerRequestFilter {
    @Autowired
    UserDetailsImplService userDetailsImplService;

    @Autowired
    AuthenticationProviderImpl authenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = "";
        String password = "";
        UsernamePasswordAuthenticationToken authenticationToken;
        String uri = request.getRequestURI(); //get path of http request

        try {
            if(uri.endsWith(SecurityConstants.REGISTER_URI_ENDING)) {
                SecurityContextHolder.getContext().setAuthentication(null);
                response.setStatus(HttpServletResponse.SC_OK);
                filterChain.doFilter(request, response);
                return;
            }
            String tokenHeader = request.getHeader(SecurityConstants.AUTH_HEADER); //get header by "Authorization"

            //check to see if authorization header exists with request
            if (tokenHeader == null || (!tokenHeader.startsWith(SecurityConstants.BASIC_TOKEN_PREFIX) && !tokenHeader.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX))) {
                log.info("No Authorization Header Found!");
                SecurityContextHolder.getContext().setAuthentication(null);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Basic Authentication (for login) NOTE:
            // Header form: Basic [Base64 encrypted username:password]
            if (tokenHeader.startsWith(SecurityConstants.BASIC_TOKEN_PREFIX) && uri.endsWith(SecurityConstants.LOGIN_URI_ENDING)) {
                tokenHeader = StringUtils.delete(tokenHeader, SecurityConstants.BASIC_TOKEN_PREFIX).trim(); //remove "Basic"
                username = JwtUtils.decodedBase64(tokenHeader)[0];
                password = JwtUtils.decodedBase64(tokenHeader)[1];
                // create auth token, validate it, and add to security context
                authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                Authentication authResult = authenticationProvider.authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authResult);

                if(SecurityContextHolder.getContext().getAuthentication() != null) {
                    log.info("Authenticated!");
                } else {
                    log.info("Authentication was bad!");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else if (tokenHeader.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX) && !uri.endsWith(SecurityConstants.LOGIN_URI_ENDING)) {
                // for all other endpoints other than /login. Must have "Bearer" prefix in Authorization header
                tokenHeader = StringUtils.delete(tokenHeader, SecurityConstants.BEARER_TOKEN_PREFIX).trim();
                if(JwtUtils.isTokenValid(tokenHeader)) {
                    username = JwtUtils.getUsernameFromJWTUserToken(tokenHeader);
                    User user = (User) userDetailsImplService.loadUserByUsername(username);
                    if(user != null) {
                        authenticationToken = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        log.info("Authorized!");
                    }
                }
            } else {
                //otherwise, trying to access endpoint with wrong authorization type.
                log.info("Wrong authentication type. Are you using Bearer or Basic?");
                SecurityContextHolder.getContext().setAuthentication(null);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (Exception e) {
            log.info("Failed to process authentication request: " + e.getLocalizedMessage());
            SecurityContextHolder.getContext().setAuthentication(null);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

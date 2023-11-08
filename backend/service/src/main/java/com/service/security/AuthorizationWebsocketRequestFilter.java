package com.service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@Order(2)
public class AuthorizationWebsocketRequestFilter extends OncePerRequestFilter {

    @Autowired
    UserDetailsImplService userDetailsImplService;

    @Autowired
    AuthenticationProviderImpl authenticationProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("Websocket auth");
            if(request.getParameter("token") != null) {
                String tokenHeader = request.getParameter("token").trim();
                if(JwtUtils.isTokenValid(tokenHeader)) {
                    String username = JwtUtils.getUsernameFromJWTUserToken(tokenHeader);
                    User user = (User) userDetailsImplService.loadUserByUsername(username);
                    if(user != null) {
                        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities()));
                        log.info("Authorized!");
                    }
                }

                if(SecurityContextHolder.getContext().getAuthentication() != null) {
                    log.info("Authenticated!");
                } else {
                    log.info("Authentication was bad!");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        } catch (Exception e) {
            log.info("Exception occured in websocket request filter: " + e.getLocalizedMessage());
            SecurityContextHolder.getContext().setAuthentication(null);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);

    }
}

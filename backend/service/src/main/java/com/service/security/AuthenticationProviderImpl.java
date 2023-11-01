package com.service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    // Bcrypt bean defined in SecurityConfiguration
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    UserDetailsImplService userDetailsImplService;

    /**
     * Attempts to authenticate a user with a given authentication object.
     * @param authentication the authentication request object.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = null;

        if(authentication == null) {
            return null;
        }

        String username = String.valueOf(authentication.getName());
        String hashedPass = String.valueOf(authentication.getCredentials());

        User user = (User) userDetailsImplService.loadUserByUsername(username);

        if (user == null) {
            return null;
        }

        if(passwordEncoder.matches(hashedPass, user.getPassword())) {
            authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
        }

        return authToken;
    }
    /**
     * Returns whether a given authentication is supported by this provider.
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}

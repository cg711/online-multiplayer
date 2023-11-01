package com.service.controller;

import com.service.DTO.RegistrationDTO;
import com.service.entity.Account;
import com.service.repository.AccountRepository;
import com.service.security.JwtUtils;
import com.service.security.SecurityConstants;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Calendar;

@RestController
@RequestMapping("/security")
@Slf4j
public class SecurityController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    /**
     * Endpoint for registering an account. POST request includes fullname, username, unhashed password.
     * @param account
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationDTO account) {
        try {
            accountRepository.save(Account.builder()
                    .role(SecurityConstants.USER_ROLE)
                    .active(true)
                    .isReported(false)
                    .dateCreated(new Date(Calendar.getInstance().getTime().getTime()))
                    .password(passwordEncoder.encode(account.getPassword())) //hash password
                    .username(account.getUsername())
                    .build());
        } catch (Exception e) {
            log.info("Exception when registering: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Route for logging in user. If program has made it to this point, it has
     * passed security
     * filters meaning the JWT is valid.
     *
     * @return
     */
    @GetMapping("/login")
    public ResponseEntity<Account> login(HttpServletResponse response) {
        try {
            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                String token = JwtUtils.generateJWTToken(username);
                log.info("Generated JWT: " + token);

                response.addHeader(SecurityConstants.AUTH_HEADER, SecurityConstants.BEARER_TOKEN_PREFIX + token);
                return ResponseEntity.ok().body(accountRepository.findByUsername(username).get(0));
            }
        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(null);
    }
}

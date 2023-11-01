package com.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class JwtUtils {
    private static Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SecurityConstants.JWT_SECRET),
            SignatureAlgorithm.HS512.getJcaName());

    /**
     * Generates a new JWT token based on a given username.
     * TODO username field subject to change.
     * @param username
     * @return JWT token
     */
    public static String generateJWTToken(String username) {

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.EXPIRATION_TIME);

        //creates and returns new JWT token using jwt library
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(hmacKey)
                .compact();
    }

    /**
     * Retrieves an accounts username from a JWT token.
     * @param token
     * @return username
     */
    public static String getUsernameFromJWTUserToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token);

        return claims.getBody().getSubject();
    }

    /**
     * Checks to see if a given JWT is valid (e.g. not expired or incorrect).
     * @param token
     * @return
     */
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(token);
            //if above executes without error, token is valid.
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect.");
        }
    }

    /**
     * Decodes base 64 JWT and returns credentials.
     * @param token
     * @return
     */
    public static String[] decodedBase64(String token) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(token);
            String pairedCredentials = new String(decodedBytes);
            String[] credentials = pairedCredentials.split(":", 2); //grabs signature
            return credentials;
        } catch (Exception e) {
            log.info("Error occured in decoding token.");
            return null;
        }
    }
}

package com.service.configuration;

import com.service.security.AuthenticationProviderImpl;
import com.service.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
public class WebsocketHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    AuthenticationProviderImpl authenticationProvider;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        try {
            String tokenHeader = request.getURI().getQuery().trim().split("&")[0].split("=")[1];
            if(tokenHeader != null) {
                log.info("Websocket token: " + tokenHeader);
                // create auth token, validate it, and add to security context
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(JwtUtils.decodedBase64(tokenHeader)[0], JwtUtils.decodedBase64(tokenHeader)[1]);
                Authentication authResult = authenticationProvider.authenticate(authenticationToken);

                if(authResult.isAuthenticated()) {
                    log.info("Handshake was good");
                    response.getHeaders().add("Authorization", "Bearer " + tokenHeader);
                    attributes.put("Authorization", "Bearer " + tokenHeader);
                    return true; //continue with handshake
                } else {
                    log.info("Authentication was bad!");
                    return false;
                }
            } else {
                log.info("No token was provided to socket handshake");
                return false;
            }
        } catch (Exception e) {
            log.info("Exception occured in websocket request filter: " + e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}

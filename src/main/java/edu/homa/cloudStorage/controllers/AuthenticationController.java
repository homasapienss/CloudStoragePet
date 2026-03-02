package edu.homa.cloudStorage.controllers;

import edu.homa.cloudStorage.dto.auth.req.SignUpRequest;
import edu.homa.cloudStorage.dto.auth.resp.SignUpResponse;
import edu.homa.cloudStorage.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth/")
@Slf4j
public class AuthenticationController {
    private final AuthService authService;
    private final SecurityContextRepository securityContextRepository;
    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(AuthService authService,
                                    SecurityContextRepository securityContextRepository,
                                    SessionAuthenticationStrategy sessionAuthenticationStrategy,
                                    AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.securityContextRepository = securityContextRepository;
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("sign-up")
    public ResponseEntity<SignUpResponse> signUp(
            @RequestBody SignUpRequest req,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        SignUpResponse out = authService.signUp(req);

        Authentication auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(req.username(), req.password())
        );

        sessionAuthenticationStrategy.onAuthentication(auth, request, response);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        securityContextRepository.saveContext(context, request, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }
}

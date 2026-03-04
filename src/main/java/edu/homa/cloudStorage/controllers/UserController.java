package edu.homa.cloudStorage.controllers;

import edu.homa.cloudStorage.dto.auth.resp.SignInResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<SignInResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(new SignInResponse(authentication.getName()));
    }
}

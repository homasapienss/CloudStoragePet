package edu.homa.cloudStorage.controllers;

import edu.homa.cloudStorage.dto.auth.resp.SignUpResponse;
import edu.homa.cloudStorage.services.entitiesServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/secured")
public class SecuredController {

    private final UserService userService;
    @Autowired
    public SecuredController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<SignUpResponse>> secured() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }
}

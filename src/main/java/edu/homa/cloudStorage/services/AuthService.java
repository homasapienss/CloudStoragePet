package edu.homa.cloudStorage.services;

import edu.homa.cloudStorage.dto.auth.req.SignUpRequest;
import edu.homa.cloudStorage.dto.auth.resp.SignUpResponse;
import edu.homa.cloudStorage.entities.UserEntity;
import edu.homa.cloudStorage.services.entitiesServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        UserEntity userEntity = userService.register(signUpRequest);
        return new SignUpResponse(userEntity.getUsername());
    }
}

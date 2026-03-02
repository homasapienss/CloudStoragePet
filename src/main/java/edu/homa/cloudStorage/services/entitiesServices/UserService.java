package edu.homa.cloudStorage.services.entitiesServices;

import edu.homa.cloudStorage.dto.auth.req.SignUpRequest;
import edu.homa.cloudStorage.dto.auth.resp.SignUpResponse;
import edu.homa.cloudStorage.entities.RoleEntity;
import edu.homa.cloudStorage.entities.UserEntity;
import edu.homa.cloudStorage.exceptions.UserAlreadyExistsException;
import edu.homa.cloudStorage.mappers.UserMapper;
import edu.homa.cloudStorage.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public List<SignUpResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toSignUpResponse)
                .toList();
    }

    public UserEntity register(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username())){
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
        }
        UserEntity userEntity = userMapper.toEntity(signUpRequest.username(), passwordEncoder.encode(signUpRequest.password()));
        RoleEntity roleEntity = roleService.findByName("ROLE_USER");
        userEntity.getRoles().add(roleEntity);
        return userRepository.save(userEntity);
    }

    public void delete(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }
}

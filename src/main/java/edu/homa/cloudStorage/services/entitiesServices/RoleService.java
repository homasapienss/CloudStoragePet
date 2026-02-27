package edu.homa.cloudStorage.services.entitiesServices;

import edu.homa.cloudStorage.entities.RoleEntity;
import edu.homa.cloudStorage.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleEntity findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role with name " + name + " not found"));
    }

    public RoleEntity addRole(RoleEntity roleEntity) {
        return roleRepository.save(roleEntity);
    }

    public RoleEntity findById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role with id " + id + " not found"));
    }

    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }
}

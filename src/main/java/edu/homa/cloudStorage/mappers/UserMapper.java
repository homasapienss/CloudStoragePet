package edu.homa.cloudStorage.mappers;

import edu.homa.cloudStorage.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "passwordHash")
    @Mapping(target = "roles",  ignore = true)
    UserEntity toEntity(String username, String passwordHash);
}

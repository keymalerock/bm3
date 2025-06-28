package com.bm3.accounting.mappers;

import com.bm3.accounting.dto.UserRequestDTO;
import com.bm3.accounting.dto.UserResponseDTO;
import com.bm3.accounting.entity.User;
import com.bm3.accounting.entity.Rol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //@Mapping(target = "roles", expression = "java(user.getRolesMtM().stream().map(Rol::getName).collect(java.util.stream.Collectors.toSet()))")
    @Mapping(target = "roles", source = "user")
    UserResponseDTO userToUserResponseDTO(User user);

    @Mapping(target = "rolesMtM", ignore = true) // Ignora el mapeo de roles por ahora, ya que no viene en el RequestDTO
    @Mapping(target = "createDate", ignore = true) // La fecha de creación se maneja en el @PrePersist de User
    @Mapping(target = "idUser", ignore = true)
    User userRequestDTOToUser(UserRequestDTO userRequestDTO);

    default Set<String> mapRolesToStrings(User user) {
        if (user == null || user.getRolesMtM() == null) {
            return new HashSet<>();
        }
        return user.getRolesMtM().stream()
                .map(Rol::getName)
                .collect(Collectors.toSet());
    }
}

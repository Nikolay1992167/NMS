package ru.clevertec.userservice.mapper.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.clevertec.userservice.model.Role;
import ru.clevertec.userservice.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final RoleRepository roleRepository;

    @Named("setRolesList")
    public List<Role> setRolesList(String roleName) {
        return new ArrayList<>(List.of(roleRepository.findByName(roleName)));
    }
}
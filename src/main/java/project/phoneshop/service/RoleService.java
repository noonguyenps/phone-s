package project.phoneshop.service;

import project.phoneshop.model.entity.RoleEntity;
import project.phoneshop.model.payload.response.role.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<RoleEntity> getAllRoles();

    RoleResponse getRoleResponse(RoleEntity role);

    RoleEntity findById(UUID uuid);
}

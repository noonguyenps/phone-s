package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.RoleEntity;
import project.phoneshop.model.payload.response.role.RoleResponse;
import project.phoneshop.repository.RoleRepository;
import project.phoneshop.service.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public List<RoleEntity> getAllRoles(){
        List<RoleEntity> roleEntities = roleRepository.findAll();
        return roleEntities;
    }
    @Override
    public RoleResponse getRoleResponse(RoleEntity role){
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        return response;
    }
    @Override
    public RoleEntity findById(UUID uuid){
        Optional<RoleEntity> role = roleRepository.findById(uuid);
        if(role.isEmpty()){
            return null;
        }
        return role.get();
    }

}

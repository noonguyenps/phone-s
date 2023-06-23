package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.RoleEntity;
import project.phoneshop.repository.RoleRepository;
import project.phoneshop.service.RoleService;

import java.util.List;

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
}

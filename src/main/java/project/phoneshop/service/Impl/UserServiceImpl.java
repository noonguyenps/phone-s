package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.RoleEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.response.user.UserResponse;
import project.phoneshop.repository.RoleRepository;
import project.phoneshop.repository.UserRepository;
import project.phoneshop.service.UserService;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    @Override
    public int getCountUser(){
        List<UserEntity> listUser = userRepository.findAll();
        return listUser.size();
    }
    @Override
    public UserEntity findById(UUID uuid){
        Optional<UserEntity> user = userRepository.findById(uuid);
        if(user.isEmpty())
            return null;
        return user.get();
    }
    @Override
    public List<UserEntity> getAllUser(int page, int size){
        Pageable paging = PageRequest.of(page, size);
        return userRepository.findAll(paging).toList();
    }
    @Override
    public UserEntity saveUser(UserEntity user, String roleName) {
        Optional<RoleEntity> role=roleRepository.findByName("USER");
        if(user.getRoles()==null){
            Set<RoleEntity> RoleSet=new HashSet<>();
            RoleSet.add(role.get());
            user.setRoles(RoleSet);
        }
        else{
            user.getRoles().add(role.get());
        }
        return userRepository.save(user);
    }
    @Override
    public UserEntity saveInfo(UserEntity user) {
        return userRepository.save(user);
    }
    @Override
    public UserEntity findByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            return null;
        }
        return user.get();    }
    @Override
    public UserEntity findByPhone(String phone) {
        Optional<UserEntity> user = userRepository.findByPhone(phone);
        if (user.isEmpty()){
            return null;
        }
        return user.get();
    }
    @Override
    public Boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }
    @Override
    public List<Object> countUserPerMonth(){
        return userRepository.countUserPerMonth();
    }

    @Override
    public UserEntity updateActive(UserEntity user) {
        user.setActive(true);
        return userRepository.save(user);
    }

    @Override
    public UserResponse getUserResponse(UserEntity user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFullName(user.getFullName());
        userResponse.setEmail(user.getEmail());
        userResponse.setGender(user.getGender());
        userResponse.setNickName(user.getNickName());
        userResponse.setPhone(user.getPhone());
        userResponse.setBirthDate(user.getBirthDate());
        userResponse.setImg(user.getImg());
        userResponse.setStatus(user.isStatus());
        userResponse.setActive(user.isActive());
        userResponse.setCountry(user.getCountry());
        userResponse.setCreateAt(user.getCreateAt());
        userResponse.setUpdateAt(user.getUpdateAt());
        userResponse.setFacebookAuth(user.getFacebookAuth());
        userResponse.setGoogleAuth(user.getGoogleAuth());
        userResponse.setAddress(user.getAddress());
        userResponse.setCountProductFavorite(user.getFavoriteProducts().size());
        double orderTotal = 0;
        for(OrderEntity order: user.getListOrder()){
            orderTotal += order.getTotal();
        }
        userResponse.setCountOrderTotal(orderTotal);
        userResponse.setCountOrder(user.getListOrder().size());
        return userResponse;
    }
}

package project.phoneshop.security.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.repository.UserRepository;
import project.phoneshop.security.DTO.AppUserDetail;

import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserDetailService implements UserDetailsService {
    private static final Logger LOGGER = LogManager.getLogger(AppUserDetailService.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findById(UUID.fromString(id));
        if(userEntity.isEmpty())
        {
            throw new UsernameNotFoundException("User not found");
        }
        LOGGER.info(userEntity.get().getEmail());
        return AppUserDetail.build(userEntity.get());
    }
}

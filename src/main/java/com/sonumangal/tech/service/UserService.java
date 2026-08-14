package com.sonumangal.tech.service;

import com.sonumangal.tech.entity.UserEntity;
import com.sonumangal.tech.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createUser(UserEntity userEntity) {
        if (userEntity.getApiEntities() != null) {
            userEntity.getApiEntities()
                    .forEach(api -> api.setUserEntity(userEntity));
        }

        userRepository.save(userEntity);
        return "Success";
    }

    public List<UserEntity> getUser() {
        return userRepository.findAll();
    }
}

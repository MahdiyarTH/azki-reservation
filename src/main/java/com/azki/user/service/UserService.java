package com.azki.user.service;

import com.azki.user.entity.UserEntity;
import com.azki.user.model.crud.CreateUserRequest;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    void createUser(CreateUserRequest request);

    void createUsers(Collection<CreateUserRequest> request);

    UserEntity get(long id);

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByUsername(String username);

}

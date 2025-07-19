package com.azki.user.service;

import com.azki.user.entity.UserEntity;
import com.azki.user.model.crud.CreateUserRequest;

import java.util.Collection;

public interface UserService {

    void createUser(CreateUserRequest request);

    void createUsers(Collection<CreateUserRequest> request);

    UserEntity get(long id);

}

package com.azki.user.service;

import com.azki.user.entity.UserEntity;
import com.azki.user.model.crud.CreateUserRequest;
import com.azki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createUser(CreateUserRequest request) {
        userRepository.save(
                UserEntity.builder()
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .email(request.getEmail())
                        .build()
        );
    }

    @Override
    public void createUsers(Collection<CreateUserRequest> requests) {
        LinkedList<UserEntity> users = new LinkedList<>();
        requests.forEach(request -> {
            UserEntity user = new UserEntity();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());

            users.add(user);
        });

        userRepository.saveAll(users);
    }

    @Override
    public UserEntity get(long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}

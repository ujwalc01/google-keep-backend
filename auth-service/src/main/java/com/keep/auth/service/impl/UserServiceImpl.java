package com.keep.auth.service.impl;

import com.keep.auth.dto.RegisterRequest;
import com.keep.auth.dto.UserResponse;
import com.keep.auth.entity.Role;
import com.keep.auth.entity.User;
import com.keep.auth.entity.UserStatus;
import com.keep.auth.exception.BusinessException;
import com.keep.auth.repository.RoleRepository;
import com.keep.auth.repository.UserRepository;
import com.keep.auth.service.UserService;
import java.util.Collections;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerNewUser(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BusinessException("EMAIL_ALREADY_USED", "Email is already registered");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_USER");
                    return roleRepository.save(r);
                });

        User user = new User();
        user.setEmail(request.getEmail().toLowerCase());
        user.setFullName(request.getFullName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }

    @Override
    public User findByEmailOrThrow(String email) {
        return userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found"));
    }

    @Override
    public UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName());
    }
}

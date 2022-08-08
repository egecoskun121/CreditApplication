package com.egecoskun.finalproject.services;

import com.egecoskun.finalproject.exception.ApplicantNotFoundException;
import com.egecoskun.finalproject.exception.CustomJwtException;
import com.egecoskun.finalproject.exception.UserNotFoundException;
import com.egecoskun.finalproject.model.Role;
import com.egecoskun.finalproject.model.User;
import com.egecoskun.finalproject.repository.UserRepository;
import com.egecoskun.finalproject.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public String signin(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
        } catch (AuthenticationException e) {
            log.error("Invalid username/password supplied");
            throw new CustomJwtException("Invalid username/password supplied", HttpStatus.BAD_REQUEST);
        }
    }

    public String signup(User user, boolean isAdmin) {
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Role role = isAdmin ? Role.ROLE_ADMIN : Role.ROLE_CLIENT;
            user.setRoles(Collections.singletonList(role));
            userRepository.save(user);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        } else {
            log.error("Username is already in use");
            throw new CustomJwtException("Username is already in use", HttpStatus.BAD_REQUEST);
        }
    }

    public void delete(String username) {
        User byUsername = userRepository.findByUsername(username);
        if (byUsername == null) {
            log.error("User could not found by username: "+username);
            throw new UserNotFoundException("User", "username : " + username);
        } else if (byUsername.getRoles().contains(Role.ROLE_ADMIN)) {
            log.error("No permission to delete user: "+username);
            throw new AccessDeniedException("No permission to delete user : " + username);
        }
        userRepository.deleteByUsername(username);
    }

    public User search(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("The user does not exist");
            throw new CustomJwtException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return user;
    }


}
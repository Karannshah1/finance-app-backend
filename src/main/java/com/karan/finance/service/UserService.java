package com.karan.finance.service;

import com.karan.finance.entity.User;
import com.karan.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findById(email);
    }
}
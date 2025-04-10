package com.ombagoes.springrestjwt.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> allUsers() {
        return new ArrayList<>(userRepository.findAll());
    }
    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }
}
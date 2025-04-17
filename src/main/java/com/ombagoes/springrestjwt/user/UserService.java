package com.ombagoes.springrestjwt.user;

import com.ombagoes.springrestjwt.user.dtos.UpdateProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String updateUser(UpdateProfileDto input) {
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticationToken.getName();
        Optional<User> userData=getUser(username);
        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setName(input.getName());
            _user.setEmail(input.getEmail());
            try {
                userRepository.save(_user);
            } catch (Exception e) {
                return e.getCause().getCause().getMessage();
            }
            return "";
        }
        return "User not exist.";
    }
    public String deleteUser() {
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticationToken.getName();
        Optional<User> userData=getUser(username);
        if (userData.isPresent()) {
            User _user = userData.get();
            try {
                userRepository.delete(_user);
            } catch (Exception e) {
                return e.getCause().getCause().getMessage();
            }
            return "";
        }
        return "User not exist.";
    }
}
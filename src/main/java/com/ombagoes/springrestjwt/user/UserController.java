package com.ombagoes.springrestjwt.user;

import com.ombagoes.springrestjwt.auth.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> allUsers() {
        return userService.allUsers();
    }

    @GetMapping("/profile")
    public UserDetails getProfile() {
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        String username= authenticationToken.getName();
        return customUserDetailsService.loadUserByUsername(username);
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser() {
        HashMap<String, Object> resultMap = new HashMap<>();
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticationToken.getName();
        Optional<User> me=userService.getUser(username);
        resultMap.put("success", true);
        resultMap.put("user", Map.of(
            "username"  , me.orElseThrow().getEmail(),   // key , value
            "role"  , me.orElseThrow().getRole().getId()
        ));

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}

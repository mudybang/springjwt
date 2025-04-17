package com.ombagoes.springrestjwt.user;

import com.ombagoes.springrestjwt.auth.CustomUserDetailsService;
import com.ombagoes.springrestjwt.user.dtos.UpdateProfileDto;
import com.ombagoes.springrestjwt.util.JwtUtil;
import com.ombagoes.springrestjwt.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationUtil validationUtil;

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
            "username"  , me.orElseThrow().getEmail(),
            "role"  , me.orElseThrow().getRole().getId()
        ));
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken() {
        HashMap<String, Object> resultMap = new HashMap<>();
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me=userService.getUser(authenticationToken.getName());
        resultMap.put("success", true);
        resultMap.put("access_token", jwtUtil.generateToken(me.orElseThrow().getId(), authenticationToken.getName()));
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateProfileDto input, BindingResult bindingResult){
        String validationMessage=validationUtil.doValidation(bindingResult);
        HashMap<String, Object> resultMap = new HashMap<>();
        if(!validationMessage.isEmpty()) {
            resultMap.put("success", false);
            resultMap.put("message", validationMessage);
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }else{
            String message=userService.updateUser(input);
            if(message.isEmpty()){
                resultMap.put("success", true);
                resultMap.put("message", "Update success.");
                return new ResponseEntity<>(resultMap, HttpStatus.OK);
            }else{
                resultMap.put("success", false);
                resultMap.put("message", message);
                return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> delete(){
        HashMap<String, Object> resultMap = new HashMap<>();
        String message=userService.deleteUser();
        if(message.isEmpty()){
            resultMap.put("success", true);
            resultMap.put("message", "Drop success.");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }else{
            resultMap.put("success", false);
            resultMap.put("message", message);
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
    }
}

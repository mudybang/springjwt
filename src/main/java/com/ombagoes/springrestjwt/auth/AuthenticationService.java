package com.ombagoes.springrestjwt.auth;

import com.ombagoes.springrestjwt.role.RoleRepository;
import com.ombagoes.springrestjwt.user.User;
import com.ombagoes.springrestjwt.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthenticationService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  AuthenticationManager authenticationManager;

    public String signup(User input) {
        input.setPassword(passwordEncoder.encode(input.getPassword()));
        Optional<User> user = userRepository.findByEmail(input.getEmail());
        if (user.isPresent()){
            return "Username exists";
        }
        try {
            userRepository.save(input);
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return "Signup Failed.";
        }
        return "";
    }

    public Long authenticate(AuthenticationRequest input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );
            return userRepository.findByEmail(input.getUsername()).orElseThrow().getId();
        }catch (BadCredentialsException e){
            log.info("Invalid username or password");
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return 0L;
    }
}

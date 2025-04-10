package com.ombagoes.springrestjwt.auth;

import com.ombagoes.springrestjwt.auth.payload.AuthenticationRequest;
import com.ombagoes.springrestjwt.user.User;
import com.ombagoes.springrestjwt.user.UserRepository;
import com.ombagoes.springrestjwt.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Object> addNewUser(@Valid @RequestBody User userInfo, BindingResult result) {
        StringBuilder builder = new StringBuilder();
        List<FieldError> errors = result.getFieldErrors();
        HttpStatus status=HttpStatus.OK;
        HashMap<String, Object> resultMap = new HashMap<>();
        for (FieldError error : errors ) {
            builder.append(error.getField()).append(" : ").append(error.getDefaultMessage()).append(", ");
        }
        if(!builder.isEmpty()) {
            resultMap.put("success", false);
            resultMap.put("message", builder.substring(0,builder.length() - 2));
            status=HttpStatus.BAD_REQUEST;
        }else{
            String message=authenticationService.signup(userInfo);
            if(message.isEmpty()){
                resultMap.put("success", true);
                resultMap.put("message", "Registration success.");
            }else{
                resultMap.put("success", false);
                resultMap.put("message", message);
                status=HttpStatus.BAD_REQUEST;
            }
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> createToken(@RequestBody AuthenticationRequest request) {
        Long authenticatedUser = authenticationService.authenticate(request);

        // Generate the token
        HashMap<String, Object> resultMap = new HashMap<>();
        if(authenticatedUser>0){
            log.info("success");
            String jwtToken = jwtUtil.generateToken(authenticatedUser, request.getUsername());
            resultMap.put("success", true);
            resultMap.put("jwtToken", jwtToken);
            resultMap.put("username", request.getUsername());
            return new ResponseEntity<>(resultMap,HttpStatus.OK);
        }else{
            log.info("failed");
            resultMap.put("success", false);
            resultMap.put("message", "Bad Credential");
            return new ResponseEntity<>(resultMap,HttpStatus.BAD_REQUEST);
        }

    }
}

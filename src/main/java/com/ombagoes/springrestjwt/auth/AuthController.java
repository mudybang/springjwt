package com.ombagoes.springrestjwt.auth;

import com.ombagoes.springrestjwt.user.User;
import com.ombagoes.springrestjwt.user.UserRepository;
import com.ombagoes.springrestjwt.util.JwtUtil;
import com.ombagoes.springrestjwt.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("auth")
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

    @Autowired
    private ValidationUtil validationUtil;

    @PostMapping("/register")
    public ResponseEntity<Object> addNewUser(@Valid @RequestBody User userInfo, BindingResult bindingResult) {
        String validationMessage=validationUtil.doValidation(bindingResult);
        HashMap<String, Object> resultMap = new HashMap<>();
        HttpStatus status=HttpStatus.OK;
        if(!validationMessage.isEmpty()) {
            resultMap.put("success", false);
            resultMap.put("message", validationMessage);
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
    public ResponseEntity<Object> createToken(@Valid @RequestBody AuthenticationRequest request, BindingResult bindingResult) {
        String validationMessage=validationUtil.doValidation(bindingResult);
        if(!validationMessage.isEmpty()) {
            return new ResponseEntity<>(Map.of(
                    "success"  , false,
                    "message"  ,  validationMessage
            ),HttpStatus.BAD_REQUEST);
        }
        Long authenticatedUser = authenticationService.authenticate(request);
        if(authenticatedUser>0){
            log.info("success");
            // Generate the token
            String jwtToken = jwtUtil.generateToken(authenticatedUser, request.getUsername());
            return new ResponseEntity<>(Map.of(
                    "success"  , true,
                    "jwtToken"  , jwtToken,
                    "username"  , request.getUsername()
            ),HttpStatus.OK);
        }else{
            log.info("failed");
            return new ResponseEntity<>(Map.of(
                    "success"  , false,
                    "message"  , "Bad Credential"
            ),HttpStatus.UNAUTHORIZED);
        }
    }
}

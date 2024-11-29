package com.streak.dsastreak.controller;

import com.streak.dsastreak.entity.JwtRequest;
import com.streak.dsastreak.entity.JwtResponse;
import com.streak.dsastreak.entity.UserRegisterDto;
import com.streak.dsastreak.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PreAuthorize("permitAll()")
    @GetMapping("/public")
    public ResponseEntity<String> publicUser() {
        return ResponseEntity.ok("Hi, I am a Public User");
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody UserRegisterDto userRegisterDto) {
        try {
            String response = userService.addUser(userRegisterDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created for successful registration
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request for invalid username
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error for unexpected issues
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
        try {
            JwtResponse response = userService.login(jwtRequest);
            return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK for successful login
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED); // 401 Unauthorized for invalid credentials
        }
    }
}

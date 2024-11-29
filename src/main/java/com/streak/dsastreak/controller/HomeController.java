package com.streak.dsastreak.controller;

import com.streak.dsastreak.entity.User;
import com.streak.dsastreak.entity.UserLoginDto;
import com.streak.dsastreak.entity.UserRegisterDto;
import com.streak.dsastreak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")

public class HomeController {

    @Autowired
    private UserService userService;

    @PreAuthorize("permitAll()")
    @GetMapping("")
    public ResponseEntity<String> Home()
    {
        return ResponseEntity.ok("HOME PAGE");
    }

    @PreAuthorize("permitAll()")
    @PostMapping("")
    public ResponseEntity<String> postHome()
    {
        return ResponseEntity.ok("HOME PAGE POST");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/normal")
    public ResponseEntity<String> normalUser()
    {
        return ResponseEntity.ok("Hi , i am a Normal User ");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> adminUser()
    {
        return ResponseEntity.ok("Hi , i am a Admin User ");
    }













}
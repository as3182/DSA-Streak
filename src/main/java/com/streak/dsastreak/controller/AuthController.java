//package com.streak.dsastreak.controller;
//
//import com.streak.dsastreak.config.CustomUserDetailService;
//import com.streak.dsastreak.entity.JwtRequest;
//import com.streak.dsastreak.entity.JwtResponse;
//import com.streak.dsastreak.security.JwtHelper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController
//{
//    @Autowired
//    private CustomUserDetailService userDetailService;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtHelper jwtHelper;
//
//    private Logger logger = LoggerFactory.getLogger(AuthController.class);
//
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> login (@RequestBody JwtRequest jwtRequest)
//    {
//        this.doAuthenticate(jwtRequest.getUsername(),jwtRequest.getPassword());
//
//        UserDetails userDetails = userDetailService.loadUserByUsername(jwtRequest.getUsername());
//        String token = this.jwtHelper.generateToken(userDetails);
//
//        JwtResponse response = JwtResponse.builder()
//                .jwtToken(token)
//                .username(userDetails.getUsername())
//                .build();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    private void doAuthenticate(String username, String password) {
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,password);
//        try{
//            authenticationManager.authenticate(authentication);}
//        catch(BadCredentialsException e)
//        {
//            throw new BadCredentialsException("Invalid username or password");
//        }
//        }
//
//        @ExceptionHandler(BadCredentialsException.class)
//        public String exceptionHandler()
//        {
//            return "Invalid Credentials";
//        }
//    }
//
//

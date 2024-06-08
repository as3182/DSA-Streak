package com.streak.dsastreak.controller;

import com.streak.dsastreak.entity.*;
import com.streak.dsastreak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/userinfo")
    public ResponseEntity<User> getUserInfo()
    {
        return userService.getUserInfo();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/alluserinfo")
    public List<User> getAllUserInfo()
    {
        return userService.getAllUserInfo();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/checkpassword")
    public ResponseEntity<String> checkPassword(UserLoginDto userLoginDto)
    {
        String response = userService.checkPassword(userLoginDto);
        if (response.equals("Correct UserName Password"))
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
        else return new ResponseEntity<>(response,HttpStatus.EXPECTATION_FAILED);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/streakstatus")
    public StreakStatusDTO getStreakdetails()
    {
        return userService.getStreakdetails();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updatestreak")
    public ResponseEntity<String> updateStreak(@RequestParam int numberOfQsns)
    {
        return userService.updateStreak(numberOfQsns);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/leaderboard")
    public List<LeaderBoardDTO> getLeaderboard()
    {
        return userService.getLeaderboard();
    }















//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<String> addUser(@RequestBody UserRegisterDto userRegisterDto) {
//        String response = userService.addUser(userRegisterDto);
//        if (response.startsWith("User Registered")) {
//            return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created for successful registration
//        } else {
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 Bad Request for existing User etc.
//        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto) {
//        String loginStatus = userService.login(userLoginDto);
//        if (loginStatus.startsWith("Invalid")) {
//            return new ResponseEntity<>(loginStatus, HttpStatus.UNAUTHORIZED);
//
//        } else {
//             // 401 Unauthorized for failed login
//            return new ResponseEntity<>(loginStatus, HttpStatus.OK); // 200 OK for successful login
//        }
//    }
//
//
//
//    // Consider adding more endpoints for other functionalities as needed
}

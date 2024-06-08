package com.streak.dsastreak.service;

//import com.streak.dsastreak.entity.User;
//import com.streak.dsastreak.entity.UserLoginDto;
//import com.streak.dsastreak.entity.UserRegisterDto;
import com.streak.dsastreak.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

//
@Service
public interface UserService {
    public ResponseEntity<User> getUserInfo();

    public String addUser(UserRegisterDto userRegisterDto);

    public List<User> getAllUserInfo();

    public String checkPassword(UserLoginDto userLoginDto);

    public ResponseEntity<String> updateStreak(int numberOfQsns);

    public StreakStatusDTO getStreakdetails();

    public List<LeaderBoardDTO> getLeaderboard();


//    public String addUser(UserRegisterDto userRegisterDto);
//
//    public String login(UserLoginDto userLoginDto);
//
//    public String generateToken(String username);
}

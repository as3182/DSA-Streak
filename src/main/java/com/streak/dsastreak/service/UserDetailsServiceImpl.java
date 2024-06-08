//package com.streak.dsastreak.service;
//
//import com.streak.dsastreak.entity.User;
//import com.streak.dsastreak.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepo;
//    @Override
//    public User loadUserByUsername(String username) throws Exception {
//        User User = userRepo.findByUserName(username);
//        if (User == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
////        }
//        return User;
//    }
//}

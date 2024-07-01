package com.streak.dsastreak.service;

//import com.streak.dsastreak.entity.User;
//import com.streak.dsastreak.entity.UserLoginDto;
//import com.streak.dsastreak.entity.UserRegisterDto;

import com.streak.dsastreak.entity.*;
import com.streak.dsastreak.repository.RoleRepository;
import com.streak.dsastreak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

//
//import java.util.Date;
//
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private UserRepository userRepository;
    @Override
    public ResponseEntity<User> getUserInfo() {

        String username = getUsernameFromSecurityContext();
        System.out.println(username + " : username");
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User Not Found: " + username));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private String getUsernameFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            // Check for email or fallback to sub (unique identifier)
            String email = oAuth2User.getAttribute("email");
            if (email != null) {
                return email;
            } else {
                String sub = oAuth2User.getAttribute("sub");
                if (sub != null) {
                    return sub;
                }
            }
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        } else {
            throw new RuntimeException("Auth principal is not a recognized user");
        }
        throw new RuntimeException("Cannot extract username from authentication principal");
    }



//

////    @Autowired
////    private KeyGenerator keyGenerator;
//    private KeyGenerator keyGenerator = new KeyGenerator();
//
//
//        @Override
//        public String generateToken(String username) {
//            String secretKey = keyGenerator.generateKey();
//            // Use a library like JJWT for secure token generation
//            String jwtToken = Jwts.builder()
//                    .setSubject(username)
//                    .setIssuedAt(new Date())
//                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
//                    .signWith(SignatureAlgorithm.HS256, secretKey)
//                    .compact();
//            return jwtToken;
//        }
//
//
//
//

    //
////    private String getSecretKeyFromEnvVar() {
////        String secretKey = System.getenv("DSA_STREAK_SECRET_KEY");
////        if (secretKey == null || secretKey.isEmpty()) {
////            throw new RuntimeException("Missing environment variable DSA_STREAK_SECRET_KEY");
////        }
////        return secretKey;}
//
    @Override
    public String addUser(UserRegisterDto userRegisterDto) {
        if (userRepository.findByUserName(userRegisterDto.getUserName()).isPresent()) {
            return "Already Existing User. Please Login!"; // Registration failed
        }

        else
        {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPwd=bCryptPasswordEncoder.encode(userRegisterDto.getPassword());

       User newUser = new User();
       newUser.setUserName(userRegisterDto.getUserName());
       newUser.setPassword(encryptedPwd);
       newUser.setName(userRegisterDto.getName());

        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(()->new RuntimeException("Role Not Found"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        newUser.setRoles(roles);

        Streaks streaks = new Streaks();
        streaks.setUser(newUser);
        streaks.setDaysOfStreaks(0);
        streaks.setNumberOfQsns(0);
        streaks.setLastUpdated(LocalDate.now());
        newUser.setStreaks(streaks);



        userRepository.save(newUser);
        return "User Registered Successfully!"; }// Registration successful
    }

    @Override
    public List<User> getAllUserInfo() {
        return userRepository.findAll();
    }

    @Override
    public String checkPassword(UserLoginDto userLoginDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails)
        {
            String HashedPassword = ((UserDetails) principal).getPassword();
            if(passwordEncoder.matches(userLoginDto.getPassword(),HashedPassword))
                return "CorrectPassword";
            else
                return "IncorrectPassword";

        }
        else
        {
            return "Wrong";
        }
    }

    @Override
    public ResponseEntity<String> updateStreak(int numberOfQsns) {
            String username = getUsernameFromSecurityContext();
            User user = userRepository.findByUserName(username).orElseThrow(() -> new RuntimeException());
            Streaks currentStreaks = user.getStreaks();

            if(currentStreaks.getLastUpdated().isEqual(LocalDate.now()) && currentStreaks.getDaysOfStreaks()!=0)
            {
                currentStreaks.setDaysOfStreaks(currentStreaks.getDaysOfStreaks());
            }
            else
            {
                currentStreaks.setDaysOfStreaks(currentStreaks.getDaysOfStreaks() + 1);
            }


            currentStreaks.setNumberOfQsns(currentStreaks.getNumberOfQsns() + numberOfQsns);
            currentStreaks.setLastUpdated(LocalDate.now());
            userRepository.save(user);

            return new ResponseEntity<>("Streak Updated ! , Keep up the good work "+user.getName(), HttpStatus.OK);

    }

    @Override
    public StreakStatusDTO getStreakdetails() {
        String currentusername = getUsernameFromSecurityContext();
        User user = userRepository.findByUserName(currentusername).orElseThrow(()->new RuntimeException("Username not Found"));
        StreakStatusDTO streakStatus = new StreakStatusDTO();
        Streaks currentUserStreaks = user.getStreaks();

        streakStatus.setName(user.getName());
        streakStatus.setUserName(currentusername);
        streakStatus.setDaysOfStreaks(currentUserStreaks.getDaysOfStreaks());
        streakStatus.setNumberOfQsns(currentUserStreaks.getNumberOfQsns());
        streakStatus.setLastUpdated(currentUserStreaks.getLastUpdated());

        return streakStatus;


    }

    @Override
    public List<LeaderBoardDTO> getLeaderboard() {
        List<LeaderBoardDTO> leaderboard = userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream().noneMatch(role -> role.getName().equals(ERole.ROLE_ADMIN)))
                .map(user -> {
                    LeaderBoardDTO dto = new LeaderBoardDTO();
                    dto.setUserName(user.getUsername());
                    Optional<Streaks> optionalStreaks = Optional.ofNullable(user.getStreaks());
                    optionalStreaks.ifPresent(streaks -> {
                        dto.setDaysOfStreaks(streaks.getDaysOfStreaks());
                        dto.setNumberOfQsns(streaks.getNumberOfQsns());
                    });
                    return dto;
                })
                .collect(Collectors.toList());

        leaderboard.sort(
                Comparator.comparingInt(LeaderBoardDTO::getDaysOfStreaks)
                        .thenComparingInt(LeaderBoardDTO::getNumberOfQsns)
                        .reversed()
                        .thenComparingInt(dto -> dto.getDaysOfStreaks() == 0 ? Integer.MAX_VALUE : dto.getDaysOfStreaks())
        );


        return leaderboard;

    }
//
//        @Override
//        public String login(UserLoginDto userLoginDto) {
//            User User = userRepo.findByUserName(userLoginDto.getUserName());
//            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//
//            if (User != null && bCryptPasswordEncoder.matches(userLoginDto.getPassword(), User.getPassword())) {
//                String token = generateToken(userLoginDto.getUserName());
//
//                return token;
//            } else {
//                return "Invalid Username or password";
//            }
//        }
    }
//
//
//
//
//

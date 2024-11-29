package com.streak.dsastreak.service;


import com.streak.dsastreak.entity.*;
import com.streak.dsastreak.repository.RoleRepository;
import com.streak.dsastreak.repository.UserRepository;
import com.streak.dsastreak.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;


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

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication != null && authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        } else {
            throw new RuntimeException("User is not authenticated");
        }
    }


    private String extractUsernameFromToken(String token) {
        try {
            return jwtHelper.getUsernameFromToken(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }


    @Override
    public String addUser(UserRegisterDto userRegisterDto) {
        String username = userRegisterDto.getUserName();

        // Log the incoming registration request
        logger.info("Attempting to register user with username: {}", username);

        // Validate username format
        if (!username.matches("^[a-zA-Z0-9._%+-]+@vrv(user|admin)$")) {
            logger.error("Invalid username format for user: {}", username);
            throw new IllegalArgumentException("Invalid username format. Must end with @vrvuser or @vrvadmin.");
        }
        logger.info("Username format is valid for: {}", username);

        // Check for existing user
        if (userRepository.findByUserName(username).isPresent()) {
            logger.warn("User already exists with username: {}", username);
            return "Already Existing User. Please Login!";
        }
        logger.info("No existing user found for username: {}", username);

        logger.info("TRYING TO SET PW");
        String encodedPassword = passwordEncoder.encode(userRegisterDto.getPassword());
        logger.info("Encoded password for user {}: {}", username, encodedPassword);

        User newUser = new User();
        newUser.setUserName(username);
        newUser.setPassword(encodedPassword);
        newUser.setName(userRegisterDto.getName());

        Set<Role> roles = new HashSet<>();

        // Assign role based on username suffix
        if (username.endsWith("@vrvuser")) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> {
                        logger.error("Role {} not found for user {}", ERole.ROLE_USER, username);
                        return new RuntimeException("Role Not Found");
                    });
            roles.add(userRole);
            logger.info("Assigned ROLE_USER to user: {}", username);
        } else if (username.endsWith("@vrvadmin")) {
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> {
                        logger.error("Role {} not found for user {}", ERole.ROLE_ADMIN, username);
                        return new RuntimeException("Role Not Found");
                    });
            roles.add(adminRole);
            logger.info("Assigned ROLE_ADMIN to user: {}", username);
        }

        newUser.setRoles(roles);

        // If user is a regular user, initialize streaks
        if (username.endsWith("@vrvuser")) {
            Streaks streaks = new Streaks();
            streaks.setUser(newUser);
            streaks.setDaysOfStreaks(0);
            streaks.setNumberOfQsns(0);
            streaks.setLastUpdated(LocalDate.now());
            newUser.setStreaks(streaks);
            logger.info("Initialized streaks for user: {}", username);
        }

        // Save the new user
        userRepository.save(newUser);
        logger.info("User registered successfully with username: {}", username);

        return "User Registered Successfully!"; // Registration successful
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

    public JwtResponse login(JwtRequest jwtRequest) {
        String username = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();

        doAuthenticate(username, password);

        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        String token = jwtHelper.generateToken(userDetails);

        return JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername())
                .build();
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}


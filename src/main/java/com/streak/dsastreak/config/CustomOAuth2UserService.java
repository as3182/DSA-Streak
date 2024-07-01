package com.streak.dsastreak.config;

import com.streak.dsastreak.entity.ERole;
import com.streak.dsastreak.entity.User;
import com.streak.dsastreak.repository.RoleRepository;
import com.streak.dsastreak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("Entering loadUser method..."); // Add this debug statement
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String,Object> attributes = oAuth2User.getAttributes();
        // Print attributes for debugging
        System.out.println("Attributes: " + attributes);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String username;
        String name;

        if(registrationId.equals("github")) {

            name = (String) oAuth2User.getAttribute("name");
            username = (String) oAuth2User.getAttribute("login");
        } else if (registrationId.equals("google")) {
            username = (String) oAuth2User.getAttribute("email");
            name = (String) oAuth2User.getAttribute("name");
        }
        else
        {
            throw new IllegalArgumentException("Unsupported Oauth2 Provider"+registrationId);
        }

        System.out.println("OAuth2 provider: " + registrationId);
        System.out.println("Retrieved name: " + name);
        System.out.println("Retrieved username: " + username);

        if (name == null || username == null) {
            throw new IllegalArgumentException("Missing required attribute(s) in user attributes");
        }

        User user = userRepository.findByUserName(username).orElseGet(() -> {
            User newUser = User.builder()
                    .userName(username)
                    .name(name)
                    .password(passwordEncoder.encode("user")) // Set a random or generated password
                    .roles(Collections.singleton(roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new UncategorizedScriptException("Default role not found"))))
                    .build();
            return userRepository.save(newUser);
        });

        // Print user details for debugging
        System.out.println("User created: " + user);

        return new DefaultOAuth2User(
                user.getAuthorities(),
                oAuth2User.getAttributes(),
                registrationId.equals("github")?"login":"email");
    }
}

package com.streak.dsastreak.config;
import com.streak.dsastreak.entity.ERole;
import com.streak.dsastreak.entity.Role;
import com.streak.dsastreak.entity.User;
import com.streak.dsastreak.repository.RoleRepository;
import com.streak.dsastreak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class AdminInitialization {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    public CommandLineRunner initAdminUser() {
        return args -> {
            if (userRepository.findByUserName("admin").isEmpty()) {
                User admin = new User();
                admin.setUserName("admin");
                admin.setPassword(passwordEncoder.encode("admin"));


                Role adminRole = getOrCreateRole(ERole.ROLE_ADMIN);
                Role userRole = getOrCreateRole(ERole.ROLE_USER);

                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                roles.add(userRole);

                admin.setRoles(roles);
                userRepository.save(admin);
            }
        };
    }

    private Role getOrCreateRole(ERole roleName) {
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        return roleOpt.orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            return roleRepository.save(role);
        });
    }
}

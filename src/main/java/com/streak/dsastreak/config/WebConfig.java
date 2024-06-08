//package com.streak.dsastreak.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
////package com.streak.dsastreak.config;
////
////import lombok.extern.slf4j.Slf4j;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.security.web.SecurityFilterChain;
////@Slf4j
////@Configuration
////@EnableWebSecurity
////public class SecurityConfig {
////    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);
////
////
//////    @Autowired
//////    private BCryptPasswordEncoder passwordEncoder; // Assuming you're using BCrypt
////
////    @Bean
////    public BCryptPasswordEncoder passwordEncoder()
////    {
////        return new BCryptPasswordEncoder();
////    }
////
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////                .authorizeRequests(auth -> auth
////                        .requestMatchers("/register").permitAll()
////                        .requestMatchers("/hello").permitAll()
////                        .requestMatchers("/login").permitAll()
//////                        .requestMatchers("/login").permitAll() // Allow /login endpoint without authentication
////                        .anyRequest().authenticated())
////                .formLogin(form -> form
////                        .loginPage("/login")
////                        .permitAll()
////                        .successHandler((request, response, authentication) -> {
////                            LOGGER.info("Authentication successful, redirecting to login (check configuration?)"); // Log a message if redirecting after login
////                            // You can replace the default behavior here if needed
////                            response.sendRedirect("/home"); // Redirect to a different page (optional)
////                        }))
////                .logout(logout -> logout
////                        .logoutUrl("/logout")
////                        .logoutSuccessUrl("/login")
////                        .permitAll());
////
////        return http.build();
////    }
////
////    // You can add other methods like passwordEncoder() and userDetailsService() here if needed
////}
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8082") // Update with your frontend URL
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
//}

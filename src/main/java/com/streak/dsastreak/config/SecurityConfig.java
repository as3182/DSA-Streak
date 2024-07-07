package com.streak.dsastreak.config;

import com.streak.dsastreak.security.JwtAuthenticationEntryPoint;
import com.streak.dsastreak.security.JwtAuthenticationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;

import static org.springframework.security.config.Customizer.*;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

//    @Autowired
//    private JwtAuthenticationFilter filter;
//
//    @Autowired
//    private JwtAuthenticationEntryPoint point;

    @Lazy
    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService();
    }

//    @Bean
//    public AuthenticationSuccessHandler successHandler() {
//        return new SimpleUrlAuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                // Do nothing upon successful authentication (no redirection)
//            }
//        };
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors->cors.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/home/public").permitAll()
                        .requestMatchers("/home/register").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .anyRequest().authenticated())
                        .oauth2Login(oauth2->oauth2
                                .userInfoEndpoint(userinfo->userinfo
                                        .userService(customOAuth2UserService))
                                .successHandler((request, response, authentication) -> {
                                    response.sendRedirect("http://localhost:3000/home");
                                }))

                        .formLogin(withDefaults())
                        .build();
//                .exceptionHandling(ex->ex.authenticationEntryPoint(point))
//                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails normalUser1 = User
//                .withUsername("Kshitiz")
//                .password(passwordEncoder().encode("Peach"))
//                .roles("USER")
//                .build();
//
//        UserDetails normalUser2 = User
//                .withUsername("Ananya")
//                .password(passwordEncoder().encode("Yushi"))
//                .roles("USER")
//                .build();
//
//        UserDetails adminUser = User
//                .withUsername("Ayush")
//                .password(passwordEncoder().encode("Nanya"))
//                .roles("ADMIN", "USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser1, normalUser2, adminUser);
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//    }
@Bean
public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("http://localhost:3000");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
}

    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

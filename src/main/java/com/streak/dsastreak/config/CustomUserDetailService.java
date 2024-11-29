package com.streak.dsastreak.config;
import com.streak.dsastreak.entity.User;
import com.streak.dsastreak.repository.UserRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.core.authority.SimpleGrantedAuthority;
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.core.userdetails.UsernameNotFoundException;
////import org.springframework.stereotype.Service;
////
////import java.util.Collection;
////import java.util.List;
////import java.util.stream.Collectors;
////
////@Service
//
////

////
////    @Override
////    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////        User user = userRepository.findByUserName(username);
////        if (user == null) {
////            throw new UsernameNotFoundException("User not found with username: " + username);
////        }
////        return new org.springframework.security.core.userdetails.User(
////                user.getUserName(),
////                user.getPassword(),
////                mapRolesToAuthorities(user.getRoles())
////        );
////    }
////
////    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
////        return roles.stream()
////                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
////                .collect(Collectors.toList());
////    }
////}
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//
@Service
public class CustomUserDetailService implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("NOT FOUND"));
        return user;
    }
}

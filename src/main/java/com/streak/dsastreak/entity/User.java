package com.streak.dsastreak.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String userName;
    private String password;
    private String name;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private Streaks streaks;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name="user_role",
    joinColumns=@JoinColumn(name="user",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="role",referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
//    @Column(columnDefinition = "bigint default 0")
//    private long streak;
//    @Column(columnDefinition = "bigint default 0")
//    private long numberOfQuestions;
}

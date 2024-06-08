package com.streak.dsastreak.repository;

import com.streak.dsastreak.entity.ERole;
import com.streak.dsastreak.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(ERole name);


}

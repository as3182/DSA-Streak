package com.streak.dsastreak.repository;
import com.streak.dsastreak.entity.Streaks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreakRepository extends JpaRepository<Streaks,Long> {
}

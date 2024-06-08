package com.streak.dsastreak.util;

import com.streak.dsastreak.entity.Streaks;
import com.streak.dsastreak.entity.User;
import com.streak.dsastreak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class StreakUpdateScheduler {

    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    private void updateStreaksWhenNotUpdated() {
        List<User> allUsers = userRepository.findAll();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        for (User user : allUsers) {
            Streaks streaks = user.getStreaks();
            if (streaks != null && streaks.getLastUpdated().isBefore(yesterday)) {
                streaks.setDaysOfStreaks(0);
                streaks.setLastUpdated(LocalDate.now());
                user.setStreaks(streaks);
                userRepository.save(user);
            }
        }
    }
}

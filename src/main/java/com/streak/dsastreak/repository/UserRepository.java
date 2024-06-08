    package com.streak.dsastreak.repository;

    import com.streak.dsastreak.entity.LeaderBoardDTO;
    import com.streak.dsastreak.entity.User;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    @Repository
    public interface UserRepository extends JpaRepository<User,Long> {

        Optional<User> findByUserName(String userName);
    }

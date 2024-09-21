package com.mo3.tictactoe.user_service.repository;

import com.mo3.tictactoe.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);
    Boolean existsByUsername(String username);


}

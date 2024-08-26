package com.cms.Repositories;

import com.cms.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//database interaction

@Repository
public interface userRepository extends JpaRepository<User, String> {
    //extra methods db related
    //custom query
    //custom finder methods

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
}

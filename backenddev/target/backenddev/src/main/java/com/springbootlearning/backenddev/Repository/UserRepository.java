package com.springbootlearning.backenddev.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.springbootlearning.backenddev.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String > {
    @Query("select u from User u where u.email = :email")
    User findByEmail(String email);

    Boolean existsByEmail(String email);
//    Boolean existsByFirstname(String firstName);

}

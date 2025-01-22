package com.scm.repositories;

import java.lang.foreign.Linker.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
//extra methods db related operations
// custom query methods


//custom finder methods
// User findByEmail(String email);
Optional<User> findByEmail(String email);
Optional<User>  findByEmailAndPassword(String email, String password);
}

package com.tuempresa.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tuempresa.entity.User;

@Repository
public interface UserDAO extends CrudRepository<User, Long> {

	Optional<User> findByUsernameAndPassword(String username, String password);
	Optional<User> findByUsername(String username);


}

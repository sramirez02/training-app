package com.tuempresa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuempresa.dao.TraineeDAO;
import com.tuempresa.dao.UserDAO;
import com.tuempresa.entity.Trainee;
import com.tuempresa.entity.User;


@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	public User getUserById(Long id) {
		return userDAO.findById(id).orElse(null);
	}
	
	public User getByUsername(String username) {
	    return userDAO.findByUsername(username).orElse(null);
	}

    public User createUser(User user) {
    	userDAO.save(user);
        return user;
    }

  	public Optional<User> authenticate(String username, String password) {
		return ((List<User>) userDAO.findAll()).stream()
				.filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findFirst();
	}

    public List<User> getAllUsers() {
        return (List<User>) userDAO.findAll();
    }
    
    
    public void updateUser(User user) {
        userDAO.save(user);
    }

	public Optional<User> findByUsername(String username) {
		return userDAO.findByUsername(username);
	}

	public void save(User user) {
        userDAO.save(user);
    }
    


}

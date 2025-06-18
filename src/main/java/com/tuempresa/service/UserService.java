package com.tuempresa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tuempresa.dao.UserDAO;
import com.tuempresa.entity.User;


@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;
	
	//AQUI MODIFICACIÓN
//	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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
    
    public boolean authenticate(String username, String password) {
        return userDAO.findByUsername(username)
        		// .map(user -> encoder.matches(password, user.getPassword()))
            .map(user -> user.getPassword().equals(password))
            .orElse(false);
    }
    
    

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isEmpty()) {
            return false; 
        }
        
        User user = userOpt.get();
        if (!user.getPassword().equals(oldPassword)) {
            return false; 
        }
        
        user.setPassword(newPassword);
        userDAO.save(user);
        return true;
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

package com.tuempresa.entity;

import java.util.Random;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Data;

@Data
@Table("app_user")
public class User {
	@Id
	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private Boolean isActive;

	@Transient
	private Trainee trainee;

	@Transient
	private Trainer trainer;

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public User() {
	}

	public User(String firstName, String lastName, boolean isActive) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.isActive = isActive;
		this.username = generateUsername(firstName, lastName);
		this.password = generatePassword();
	}

	private String generateUsername(String firstName, String lastName) {

		return firstName.toLowerCase() + "." + lastName.toLowerCase();
	}

	private String generatePassword() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder pwd = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			pwd.append(characters.charAt(random.nextInt(characters.length())));
		}
		System.out.println("111111111: " + pwd.toString());
		return encoder.encode(pwd.toString());
	}

	public boolean checkPassword(String rawPassword) {
		return encoder.matches(rawPassword, this.password);
	}

}
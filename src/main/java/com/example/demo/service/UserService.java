package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.model.UserDetail;

public interface UserService {

	public UserDetail saveUser(UserDetail userDetails);
	
	public boolean checkEmail(String email);

	public UserDetail getUserById(Long userId);

	public boolean deleteUserById(Long id);

	public UserDetail findByUsername(String username);

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
		
}

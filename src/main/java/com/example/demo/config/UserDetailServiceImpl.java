package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserDetail;
import com.example.demo.repository.userRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

	@Autowired
	private userRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserDetail user = userRepo.findByEmail(email);
		if(user != null) {
			return new CustomUserDetails(user);
		}
		
		throw new UsernameNotFoundException("user not available");
	}
	
	
}

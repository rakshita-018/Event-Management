package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.UserDetail;
import com.example.demo.repository.userRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private userRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;
	
	@Override
	public UserDetail saveUser(UserDetail user) {
		
		user.setPassword(passwordEncode.encode(user.getPassword()));
		
	    // Only set role to ROLE_USER if no role is provided (normal registration case)
	    if (user.getRole() == null || user.getRole().isEmpty()) {
	        user.setRole("ROLE_USER");  // Default to ROLE_USER only if no role is provided
	    }
		
		user.setAccLockUnlock(true);
		return userRepo.save(user);
	}

	@Override
	public boolean checkEmail(String email) {
		return userRepo.existsByEmail(email);
	}
	
	@Override
	public UserDetail getUserById(Long userId) {
	    return userRepo.findById(userId).orElse(null);
	}

	@Override
	public boolean deleteUserById(Long id) {
		if(userRepo.existsById(id)) {
			userRepo.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public UserDetail findByUsername(String username) {
		return userRepo.findByEmail(username);
	}
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetail user = userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());     }

//    private Collection<? extends GrantedAuthority> getAuthorities(UserDetail user) {
//        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
//    }
}

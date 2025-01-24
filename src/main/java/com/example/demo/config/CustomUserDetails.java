package com.example.demo.config;

import com.example.demo.model.UserDetail;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import  org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails{

	private UserDetail user;
	
	public CustomUserDetails(UserDetail user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());
		return Arrays.asList(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
	 return user.getEmail();
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return user.isAccLockUnlock();
	}

}

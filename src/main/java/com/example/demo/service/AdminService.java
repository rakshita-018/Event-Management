package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.userRepository;

@Service
public class AdminService {

    @Autowired
    private userRepository userRepo;
    
	public long getTotalUsers() {
		return userRepo.count();
	}

	public long getLockedAccounts() {
		return userRepo.countByAccLockUnlock(false);
	}

}

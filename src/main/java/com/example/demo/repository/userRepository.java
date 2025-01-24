package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserDetail;

@Repository
public interface userRepository extends JpaRepository<UserDetail, Long>{
	
	public boolean existsByEmail(String email);
	public UserDetail findByEmail(String email);
	
	public UserDetail findByEmailAndPhoneNo(String email, String phoneNo);
	
	public Optional<UserDetail> findById(Long userId);
	
	public long countByAccLockUnlock(boolean b);
	

}

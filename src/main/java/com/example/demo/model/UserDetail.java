package com.example.demo.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_details")
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "role", nullable = false)
    private String role;
    
    @Column(name = "phoneNo", nullable = false)
    private String phoneNo;
    
    @Column(name = "accLockUnlock", nullable = false)
    private boolean accLockUnlock ;
    
    @ManyToMany(mappedBy = "members")  // Use mappedBy to avoid duplication of the join table
    private List<Teams> teams;
    
    // getters and setters
    public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public boolean isAccLockUnlock() {
		return accLockUnlock;
	}

	public void setAccLockUnlock(boolean accLockUnlock) {
		this.accLockUnlock = accLockUnlock;
	}
	
    public List<Teams> getTeams() {
        return teams;
    }

    public void setTeams(List<Teams> teams) {
        this.teams = teams;
    }

	@Override
	public String toString() {
		return "UserDetail [id=" + id + ", email=" + email + ", name=" + name + ", password=" + password + ", role="
				+ role + ", phoneNo=" + phoneNo + ", accLockUnlock=" + accLockUnlock + ", teams=" + teams + "]";
	}


}


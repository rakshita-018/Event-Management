package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "teams")
public class Teams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "event_id")  // Foreign key to Event
    private Event event;

    @ManyToMany
    @JoinTable(
        name = "teams_members",  // The name of the join table
        joinColumns = @JoinColumn(name = "team_id"),  // Foreign key for Teams
        inverseJoinColumns = @JoinColumn(name = "member_id")  // Foreign key for UserDetail
    )
    private List<UserDetail> members;
    
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<UserDetail> getMembers() {
        return members;
    }

    public void setMembers(List<UserDetail> members) {
        this.members = members;
    }
}


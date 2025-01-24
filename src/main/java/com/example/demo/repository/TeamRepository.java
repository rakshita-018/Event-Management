package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Teams;
import com.example.demo.model.UserDetail;

public interface TeamRepository extends JpaRepository<Teams, Long> {

	boolean existsByMembersIdAndEventId(Long userId, Long eventId);

	
	    @Query("SELECT t FROM Teams t JOIN t.members m WHERE t.event.id = :eventId AND m.id = :userId")
	    Teams findByEventIdAndMemberId(@Param("eventId") Long eventId, @Param("userId") Long userId);


}

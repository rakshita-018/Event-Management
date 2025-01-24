package com.example.demo.repository;

import com.example.demo.model.Event;
import com.example.demo.model.Registration;
import com.example.demo.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

	Optional<Registration> findByUserAndEvent(UserDetail user, Event event);

	boolean existsByUserIdAndEventId(Long userId, Long eventId);

	Registration findByUserIdAndEventId(Long userId, Long eventid);

}

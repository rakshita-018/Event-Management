package com.example.demo.service;
import java.util.UUID;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpSession;

import com.example.demo.model.*;

@Service
public class RegistrationService {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private EventRepository eventRepository;
    
   @Autowired
   private TeamRepository teamRepository;

    @Autowired
    private RegistrationRepository registrationRepository;
    
    private final RazorpayClient razorpayClient;

    @Autowired
    public RegistrationService(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    public Order createOrder(Long userId, Long eventId) throws RazorpayException {
        UserDetail user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        // Razorpay order creation
        JSONObject orderRequest = new JSONObject();
        int amountInPaise = Math.round(event.getAmount() * 100);
        orderRequest.put("amount", amountInPaise); // Amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", user.getEmail());
        orderRequest.put("payment_capture", 1);  
        
        try {
        Order order = razorpayClient.orders.create(orderRequest);
        String orderId = order.get("id");
        
        // Save the registration with pending status
//        Registration registration = new Registration();
//        registration.setUser(user);
//        registration.setEvent(event);
//        registration.setPaymentAmount(event.getAmount());
//        registration.setPaymentStatus("PENDING");
//        registration.setRazorPayId(order.get("id"));  // Save the Razorpay order ID
//        registrationRepository.save(registration);

        return order;
    }catch (RazorpayException e) {
        System.err.println("Error creating order: " + e.getMessage());
        throw new RazorpayException("Failed to create order: " + e.getMessage());
    }
    }

    public void confirmPayment(String razorpayId, Long eventId, Long userId) {
        // Fetch the user and event from the database
        UserDetail user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        // Update the registration with payment details and status
        
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setPaymentAmount(event.getAmount());
        registration.setPaymentStatus("SUCCESS");
        registration.setRazorPayId(razorpayId);
        
        registrationRepository.save(registration);
    }

	public Teams saveTeam(Teams team) {
		return teamRepository.save(team);
	}

	public boolean isMemberRegisteredForEvent(Long userId, Long eventId) {
		return teamRepository.existsByMembersIdAndEventId(userId, eventId);
	}
	
	public boolean isUserRegisteredForIndividualEvent(Long userId, Long eventId) {
	    return registrationRepository.existsByUserIdAndEventId(userId, eventId);
	}

	public boolean isPaymentSuccessful(Long userId, Long eventId) {
		Registration registration = registrationRepository.findByUserIdAndEventId(userId, eventId);
		return registration != null && "success".equals(registration.getPaymentStatus());
	}



}

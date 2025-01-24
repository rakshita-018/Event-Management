package com.example.demo.contoller;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Event;
import com.example.demo.model.Teams;
import com.example.demo.model.UserDetail;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.userRepository;
import com.example.demo.service.EventService;
import com.example.demo.service.RegistrationService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class RegistrationController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private RegistrationService registrationService;
    
    @Autowired
    private userRepository userRepo;
    

    @PostMapping("/checkTeamSize")
    public String checkTeamSize(@RequestParam("eventId") Long eventId,
                                @RequestParam("teamSize") int teamSize, 
                                Principal principal,
                                HttpSession session) {
    	
    	String email = principal.getName();
    	UserDetail currentUser = userRepo.findByEmail(email);
        
        // Remove any previous session message
        session.removeAttribute("msg");
        
    	if (teamSize == 1) {
            boolean isRegisteredForEvent = registrationService.isUserRegisteredForIndividualEvent(currentUser.getId(), eventId);
            
            if (isRegisteredForEvent) {
            	boolean isPaymentSuccessful = registrationService.isPaymentSuccessful(currentUser.getId(),eventId);
            	if (isPaymentSuccessful) {
            		session.setAttribute("msg", "You are already registered for this event.");
                    return "redirect:/user/events"; 
            	}
        		session.setAttribute("msg", "You are already registered for this event.");
                return "redirect:/user/events";
            }   
            // Redirect to Razorpay payment for single participant events
            return "redirect:/user/razorpay?eventId=" + eventId;
        } else {
            // Redirect to team registration form if it's a team event
        	return "redirect:/user/teamRegistration?eventId=" + eventId + "&teamSize=" + teamSize;
        }
    }
    
    @GetMapping("/razorpay")
    @PreAuthorize("isAuthenticated()") 
    public String razorpay(@RequestParam("eventId") Long eventId, Principal principal, Model model) throws RazorpayException {
        // Fetch the email of the currently logged-in user
        String email = principal.getName();
        UserDetail currentUser = userRepo.findByEmail(email);

        if (currentUser == null) {
            System.out.println("Current user is null!");
            return "redirect:/login";  // Redirect to login if user is not authenticated
        }

        // Create a Razorpay order
        Order order = registrationService.createOrder(currentUser.getId(), eventId);

        Event event = eventService.getEventById(eventId);

        model.addAttribute("orderId", order.get("id"));
        model.addAttribute("amount", event.getAmount() * 100); // Amount in paise for Razorpay
        model.addAttribute("currency", "INR");
        model.addAttribute("eventId", eventId);
        model.addAttribute("userId", currentUser.getId());
        return "user/payment";
    }


    @PostMapping("/razorpay")
    public String paymentSuccess(@RequestParam("razorpayId") String razorpayId,
                                 @RequestParam("eventId") Long eventId,
                                 Principal principal, HttpSession session) {
        String email = principal.getName();
        UserDetail currentUser = userRepo.findByEmail(email);

        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        registrationService.confirmPayment(razorpayId, eventId, currentUser.getId());
        Teams team = (Teams) session.getAttribute("team");
        if (team != null) {
            registrationService.saveTeam(team);
            session.removeAttribute("team"); 
        }
        return "user/paymentSuccess";
    }

    @GetMapping("/teamRegistration")
    public String showTeamRegistrationForm(@RequestParam("eventId") Long eventId, 
                                           @RequestParam("teamSize") int teamSize, Model model) {
        Event event = eventService.getEventById(eventId);
        
        System.out.println(teamSize);
        model.addAttribute("event", event);
        model.addAttribute("team", new Teams());
        model.addAttribute("teamSize", teamSize);
        return "user/teamRegistration"; // The name of the view for team registration
    }

    @PostMapping("/teamRegistration")
    public String registerTeam(@RequestParam("eventId") Long eventId,
                               @RequestParam("teamName") String teamName,
                               @RequestParam("teamMembers") List<String> teamMembersNames,
                               @RequestParam("teamEmails") List<String> teamMembersEmails,
                               Model model, HttpSession session) {
        // Validate the team size
        if (teamMembersEmails.size() != teamMembersNames.size()) {
            session.setAttribute("msg", "Mismatch between the number of names and emails.");
            Event event = eventService.getEventById(eventId);
            model.addAttribute("event", event);
            model.addAttribute("teamSize", teamMembersEmails.size());
            return "redirect:/user/events"; // Stay on the same page
        }

        // Validate email existence
        List<UserDetail> teamMembers = new ArrayList<>();
        for (String email : teamMembersEmails) {
            UserDetail user = userRepo.findByEmail(email);
            if (user == null) {
                session.setAttribute("msg", "Email " + email + " is not registered.");
                Event event = eventService.getEventById(eventId);
                model.addAttribute("event", event);
                model.addAttribute("teamSize", teamMembersEmails.size());
                return "redirect:/user/events"; // Stay on the same page
            }
            teamMembers.add(user);
        }
        
        

        // Check for duplicate registrations
        for (UserDetail member : teamMembers) {
            boolean isAlreadyRegistered = registrationService.isMemberRegisteredForEvent(member.getId(), eventId);
            if (isAlreadyRegistered) {
                    session.setAttribute("msg", "Member " + member.getEmail() + " is already registered for this event.");
                    Event event = eventService.getEventById(eventId);
                    model.addAttribute("event", event);
                    model.addAttribute("teamSize", teamMembersEmails.size());
                    return "redirect:/user/events"; // Stay on the same page
            }
        }

        // Save the team
        try {
            Event event = eventService.getEventById(eventId);
            Teams team = new Teams();
            team.setEvent(event);
            team.setName(teamName);
            team.setMembers(teamMembers);
            session.setAttribute("team", team);
//            registrationService.saveTeam(team);

        } catch (Exception e) {
            session.setAttribute("msg", "An error occurred: " + e.getMessage());
            Event event = eventService.getEventById(eventId);
            model.addAttribute("event", event);
            model.addAttribute("teamSize", teamMembersEmails.size());
            return "redirect:/user/events"; // Stay on the same page
        }

        // Clear the session message and redirect on success
        session.removeAttribute("msg");
        return "redirect:/user/razorpay?eventId=" + eventId; // Redirect only on success
    }
    


}



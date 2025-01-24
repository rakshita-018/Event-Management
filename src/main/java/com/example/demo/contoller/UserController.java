package com.example.demo.contoller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Event;
import com.example.demo.model.UserDetail;
import com.example.demo.repository.userRepository;
import com.example.demo.service.EventService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private userRepository userRepo;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		String email = p.getName();
		UserDetail user = userRepo.findByEmail(email);
		
		m.addAttribute("user",user);
	}
	
	@GetMapping("/")
	public String home() {
		return "user/home";
	}
	
	@GetMapping("/changePassword")
	public String loadChangePassword(Model model, HttpSession session) {
		if (session.getAttribute("msg") != null) {
            model.addAttribute("msg", session.getAttribute("msg"));
            // Remove the session attribute 'msg' after adding it to the model
            session.removeAttribute("msg");
        }
		return "user/changePassword";
	}
	
	@PostMapping("/updatePass")
	public String changePassword(Principal p, @RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass, HttpSession session) {
		
		String email = p.getName();
		UserDetail loginUser = userRepo.findByEmail(email);
		
		boolean f = passwordEncoder.matches(oldPass, loginUser.getPassword());
		
		if(f) {
			loginUser.setPassword(passwordEncoder.encode(newPass));
			UserDetail updatePasswordUser = userRepo.save(loginUser);
			if(updatePasswordUser != null) {
				session.setAttribute("msg", "Password Updated Successfully");
			}else {
				session.setAttribute("msg", "Something Wrong in server");
			}
			
			
		}else {
			session.setAttribute("msg", "Old password is incorrect");
		}
		
		
		return "redirect:/user/changePassword";
	}
	
    @GetMapping("/events")
    public String listEvents(Model model) {
        List<Event> culturalEvents = eventService.findAllCulturalEvents();
        List<Event> technicalEvents = eventService.findAllTechnicalEvents();
        List<Event> sportsEvents = eventService.findAllSportsEvents();
        model.addAttribute("culturalEvents", culturalEvents);
        model.addAttribute("technicalEvents", technicalEvents);
        model.addAttribute("sportsEvents", sportsEvents);
        return "user/events";  // Thymeleaf template name (events.html)
    }
		
}

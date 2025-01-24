package com.example.demo.contoller;

import java.security.Principal;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.model.UserDetail;
import com.example.demo.repository.userRepository;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class homeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private userRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
    @Autowired
    private AuthenticationManager authenticationManager;
    
	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		
		if(p!= null) {
			String email = p.getName();
			UserDetail user = userRepo.findByEmail(email);	
			m.addAttribute("user",user);
		}
		
	}
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/signin")
	public String login(Model model, HttpSession session) {
		if (session.getAttribute("msg") != null) {
            model.addAttribute("msg", session.getAttribute("msg"));
            // Remove the session attribute 'msg' after adding it to the model
            session.removeAttribute("msg");
        }
		return "login";
	}

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
		    new UsernamePasswordAuthenticationToken(username, password)
		);
		// If authentication is successful, the user is automatically logged in.
		return "redirect:/user/home"; // Redirect to user's home page after successful login
    }
	
	@GetMapping("/registration")
	public String registration(Model model, HttpSession session) {
		if (session.getAttribute("msg") != null) {
            model.addAttribute("msg", session.getAttribute("msg"));
            // Remove the session attribute 'msg' after adding it to the model
            session.removeAttribute("msg");
        }
	    return "createAccount";
	}

	
	@PostMapping("/createUser")
	public String createUser(@ModelAttribute UserDetail user,HttpSession session) { 
		
		boolean f = userService.checkEmail(user.getEmail());
	    String phoneNo = user.getPhoneNo();
	    String password = user.getPassword();
	    
	    // Phone number validation (e.g., should be 10 digits)
	    if (!phoneNo.matches("\\d{10}")) {
	        session.setAttribute("msg", "Phone number must be 10 digits");
	        return "redirect:/registration";
	    }
	    if (password.length() < 8) {
	        session.setAttribute("msg", "Password must be at least 8 characters long");
	        return "redirect:/registration";
	    }
	    
		if(f) {
			session.setAttribute("msg", "Email already exist");
		}
		else {
			UserDetail userDetail = userService.saveUser(user);
		    if (userDetail != null) {
		    	session.setAttribute("msg", "Registred successfully");
		    } else {
		    	session.setAttribute("msg", "Something wrong in server");
		    } 
		}
	    
	    return "redirect:/registration";
	}
	
	@GetMapping("/loadforgetPass")
	public String loadForgetPass(Model model , HttpSession session) {
		if (session.getAttribute("msg") != null) {
            model.addAttribute("msg", session.getAttribute("msg"));
            session.removeAttribute("msg");
        }
		return "forgetPass";
	}

	@PostMapping("/forgetPassword")
	public String forgetPassword(@RequestParam String email, @RequestParam String phoneNo, HttpSession session) {
		UserDetail user = userRepo.findByEmailAndPhoneNo(email, phoneNo);
		
		if(user != null) {
			return "redirect:/loadresetPass/"+user.getId();
		}else {
			session.setAttribute("msg", "invalid email and mobile nuber");
			return "redirect:/loadforgetPass";
		}		
	}
	
	@GetMapping("/loadresetPass/{id}")
	public String loadResetPass(@PathVariable int id, Model model,HttpSession session) {
		model.addAttribute("id",id);
		if(session.getAttribute("msg")!= null) {
			model.addAttribute("msg",session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		return "resetPass";
	}
	
	@PostMapping("/resetPassword")
	public String resetPassword(@RequestParam String psw,@RequestParam String cpsw, @RequestParam Long id, HttpSession session) {
		
		if(psw.equals(cpsw)) {
			UserDetail user=userRepo.findById(id).get();
			String encryptPassword = passwordEncoder.encode(psw);	
			user.setPassword(encryptPassword);		
			UserDetail updatedPass = userRepo.save(user);
			
			if(updatedPass != null) {
				session.setAttribute("msg", "Reset Password Successfull");
			}
			
			return "redirect:/signin";
		}
		else {
			session.setAttribute("msg", "Re-entered password doesnt match");
			return "redirect:/loadresetPass/"+id;
		}
		
		
	}
} 
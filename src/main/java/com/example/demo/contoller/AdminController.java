package com.example.demo.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.UserDetail;
import com.example.demo.repository.userRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.EventService;
import com.example.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private userRepository userRepo;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public String home(Model model) {
    	model.addAttribute("totalUsers", adminService.getTotalUsers());
    	model.addAttribute("lockedAccounts", adminService.getLockedAccounts());
    	model.addAttribute("totalEvents", eventService.getTotalEvents());
        return "admin/dashboard"; // Admin home page
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserDetail> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "admin/userList"; // The user list view we just created
    }

    @PostMapping("/lockUser")
    public String lockUser(Long userId) {
        UserDetail user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            user.setAccLockUnlock(true); // Lock the user account
            userRepo.save(user);
        }
        return "redirect:/admin/users"; // Redirect to the user list
    }

    @PostMapping("/unlockUser")
    public String unlockUser(Long userId) {
        UserDetail user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            user.setAccLockUnlock(false); // Unlock the user account
            userRepo.save(user);
        }
        return "redirect:/admin/users"; // Redirect to the user list
    }
   
    // Delete user by ID
    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = userService.deleteUserById(id);
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found or couldn't be deleted.");
        }
        return "redirect:/admin/users";
    }
    
    
    // Display form to add new user
    @GetMapping("/addUser")
    public String addUserForm(Model model) {
        model.addAttribute("user", new UserDetail());  // Binding form data to UserDetail object
        return "admin/addUser";  // Returns the form view
    }

    // Handle form submission to add new user
    @PostMapping("/addUser")
    public String saveNewUser(@ModelAttribute("user") UserDetail userDetail, Model model) {
        if (userService.checkEmail(userDetail.getEmail())) {
            model.addAttribute("errorMessage", "Email already exists!");
            return "admin/addUser";
        }
        
        userService.saveUser(userDetail);  // Save user via UserService
        return "redirect:/admin/users";  // Redirect to user management page after saving
    }
    

    
}

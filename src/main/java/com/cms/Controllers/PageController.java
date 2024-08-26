package com.cms.Controllers;

import com.cms.Forms.UserForm;
import com.cms.Helpers.JWTUtilHelper;
import com.cms.Helpers.Message;
import com.cms.Models.User;
import com.cms.Services.userService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PageController {

    @Autowired
    private userService userService;

    @Autowired
    private JWTUtilHelper jwtUtilHelper;

    @GetMapping("/home")
    public void getAllUsers() {
//        return "users";
    }

    @GetMapping("/about")
    public void aboutPage() {
//        return "Hi this is About page";
    }

    @GetMapping("/register")
    public void registerPage() {
//        return "register Page";
    }

    @PostMapping("/do-register")
    public ResponseEntity<?> processRegister(@Valid @RequestBody UserForm userForm, BindingResult result, HttpSession session) {
        System.out.println("Processing Register");

        if (result.hasErrors()) {
            // Return a single error message as a string
            String errorMessage = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            return ResponseEntity.status(400).body(errorMessage);
        }

        // Save user details to the database
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhone(userForm.getPhone());
        user.setGender(userForm.getGender());
        user.setProfilePic("https://images.pexels.com/photos/674010/pexels-photo-674010.jpeg");

        User savedUser = this.userService.saveUser(user);
        System.out.println("User saved: " + savedUser);

        Message message = Message
                .builder()
                .content("Registration Successful")
                .type("green")
                .build();

        // Set message in session
        session.setAttribute("message", message);
        System.out.println("Message set in session: " + session.getAttribute("message"));
        System.out.println("Session ID: " + session.getId());

//        // Return success message
//        return ResponseEntity.ok("Registration Successful");
        // Automatically log in the user by generating a JWT
        String token = this.jwtUtilHelper.generateToken(savedUser);

        // Return the token to the frontend
        return ResponseEntity.ok(token);
    }

    @GetMapping("/session-message")
    public ResponseEntity<Message> getSessionMessage(HttpSession session) {
        Message toastMessage = (Message) session.getAttribute("message");
        System.out.println("Session ID: " + session.getId());
        System.out.println("Message retrieved from session: " + toastMessage);

        // Remove the message from session after retrieving it
        session.removeAttribute("message");

        // Return the message, or an empty string if it's null
        return ResponseEntity.ok(toastMessage);
    }

}

package com.cms.Controllers;

import com.cms.Helpers.Helper;
import com.cms.Models.User;
import com.cms.Services.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private userService userService;

    public User addLoggedInUser(Authentication authentication) {
        String name = Helper.getEmailOfLoggedInUser(authentication);

        User user = this.userService.getUserByEmail(name);
        if (user == null) {
            return null;
        }

        System.out.println(name);
        return user;
    }

    //dashboard page
    @GetMapping("/dashboard")
    public void userDashboard() {

    }

    //profile page
    @GetMapping("/profile")
    public void userProfile(Authentication principal) {
//        User user = this.addLoggedInUser(principal);
//        return ResponseEntity.ok("user");
    }

    // Endpoint to get logged-in user details
    @GetMapping("/details")
    public ResponseEntity<User> getUserDetails(Authentication principal) {
        User user = this.addLoggedInUser(principal);
//        System.out.println(user);
        return ResponseEntity.ok(user);
    }

}


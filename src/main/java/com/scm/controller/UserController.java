package com.scm.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserServices;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServices userServices;
    
    //user dashboard page

    @RequestMapping(value = "/dashboard", method=RequestMethod.GET)
    public String userDashboard() {
        return "user/dashboard";
    }

    @RequestMapping(value = "/profile", method=RequestMethod.GET)
    public String userProfile(Model model, Authentication authentication) {
        
        return "user/profile";
    }
    
    

    //user add contact page

    //user view contact page

    //user update contact page

    //user delete contact page

    //user search contact page
}

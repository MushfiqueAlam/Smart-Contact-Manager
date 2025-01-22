package com.scm.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserServices;

@ControllerAdvice
public class RootController {

    @Autowired
    private UserServices userServices;
    private Logger logger = org.slf4j.LoggerFactory.getLogger(RootController.class);
    @ModelAttribute
    public void loggedInUserInformation(Model model,Authentication authentication){
        if(authentication==null){
            return;
        }
       logger.info("Add information to every page");
        String username=Helper.getEmailOfLoggedInUser(authentication);
        User user=userServices.getUserByEmail(username);

            model.addAttribute("loggedUser",user);
            System.out.println(user.getAbout());
            System.out.println(user.getName());
            System.out.println(user.getEmail());
        
        System.out.println("User profile : "+username);
        System.out.println(user);
    }

}

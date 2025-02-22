package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.repositories.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    UserRepo userRepo;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token,HttpSession session){
      
     User user=  userRepo.findByEmailToken(token).orElse(null);
     if(user!=null && user.getEmailToken().equals(token)){
        
            user.setEmailVerified(true);
            user.setEnabled(true);
            userRepo.save(user);
            session.setAttribute("message", Message.builder().type(MessageType.green).content("Email  verified! your account is enabled now you can login your scm account").build());

            return "success_page";
        
       
     }else{

         session.setAttribute("message", Message.builder().type(MessageType.red).content("Email not verified! Token is not associated with user").build());
    
            return "error_page";
     }
    }
}

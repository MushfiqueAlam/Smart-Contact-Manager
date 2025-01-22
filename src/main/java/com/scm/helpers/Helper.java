package com.scm.helpers;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
    
        public static String getEmailOfLoggedInUser(Authentication authentication){

           
            if(authentication instanceof OAuth2AuthenticationToken){
                var aOAuth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;
                var clientId=aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
                var oauth2User=(OAuth2User)authentication.getPrincipal();
                String username="";
                if(clientId.equalsIgnoreCase("google")){
                    System.out.println("This application form by google");
                    username=oauth2User.getAttribute("email").toString();
                }
                return username;
            }else{
                System.out.println("Getting data from local storage");
                return authentication.getName();
            }
           

        }
}


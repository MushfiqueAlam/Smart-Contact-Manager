package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.User;
import com.scm.entities.providers;
import com.scm.helpers.AppConstant;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

@Component
public class OAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = org.slf4j.LoggerFactory.getLogger(OAuthenticationSuccessHandler.class);
    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

                var oauth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;
                String authorizedClientRegistrationId=oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
                logger.info(authorizedClientRegistrationId);
                var oauthUser=(DefaultOAuth2User)authentication.getPrincipal();
                oauthUser.getAttributes().forEach((key,value)->{
                    logger.info(key+":"+value);
                });

                // String email = oauthUser.getAttribute("email");
                // String name = oauthUser.getAttribute("name");
                // String picture = oauthUser.getAttribute("picture");
                // String provider = authorizedClientRegistrationId.toUpperCase();

                // Optional<User> userExists = userRepo.findByEmail(email);
                // if (userExists.isEmpty()) {
                //     User newUser = new User();
                //     newUser.setEmail(email);
                //     newUser.setName(name);
                //     newUser.setProfilePic(picture != null ? picture : "/default-profile.png");
                //     newUser.setProvider(providers.valueOf(provider));
                //     newUser.setPassword("123456");
                //     newUser.setUserId(UUID.randomUUID().toString());
                //     newUser.setEnabled(true);
                //     newUser.setEmailVerified(true);
                //     newUser.setRoleList(List.of(AppConstant.USER_ROLE));                 
                //     newUser.setAbout("This account is created using " + provider + " OAuth2");

                //     userRepo.save(newUser);
                //     logger.info("User Created");

                // }

                User user=new User();
                user.setUserId(UUID.randomUUID().toString());
                user.setRoleList(List.of(AppConstant.USER_ROLE));
                user.setEmailVerified(true);
                user.setEnabled(true);
                user.setPassword("123456");
                if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
                    user.setEmail(oauthUser.getAttribute("email").toString());
                    user.setName(oauthUser.getAttribute("name"));
                    user.setProfilePic(oauthUser.getAttribute("picture"));
                    user.setProvider(providers.GOOGLE);
                    // user.setProviderId(oauthUser.getName());
                }


        // DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        // logger.info("User Name: " + user.getName());
        // user.getAttributes().forEach((k, v) -> {
        //     logger.info(k + " : " + v);
        // });
        // logger.info("User Authorities: " + authentication.getAuthorities());
        // logger.info("OAuthenticationSuccessHandler");

        // String email = user.getAttribute("email").toString();
        // String name = user.getAttribute("name").toString();
        // String picture = user.getAttribute("picture").toString();
        // String provider = user.getAttributes().get("sub").toString();

        // // Create a new user and save it to the database
        // com.scm.entities.User newUser = new com.scm.entities.User();
        // newUser.setEmail(email);
        // newUser.setUserName(name);
        // newUser.setProfilePic(picture);
        // newUser.setProvider(providers.GOOGLE);
        // newUser.setPassword("123456");
        // // newUser.setActive(true);
        // newUser.setUserId(UUID.randomUUID().toString());
        // newUser.setEnabled(true);
        // newUser.setEmailVerified(true);
        // // newUser.setProviderId(user.getName());
        // // newUser.setRoleList(List.of(AppConstant.ROLE_USER));
        // newUser.setRoleList(List.of(AppConstant.USER_ROLE));
        
        // newUser.setAbout("This account is created using Google OAuth2");

        // // User userExists = userRepo.findByEmail(email);
        // Optional<User> userExists=userRepo.findByEmail(email);
        // if (userExists == null) {
        //     userRepo.save(newUser);
        //     logger.info("User Created");
        // }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}

package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstant;
import com.scm.helpers.Helper;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.UserServices;
@Service
public class UserServiceImpl implements UserServices {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private org.slf4j.Logger logger=LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {

        //user id have to generate
        String userId=UUID.randomUUID().toString();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //set the user role
        user.setRoleList(List.of(AppConstant.USER_ROLE));
        logger.info(user.getProvider().toString());

        String emailToken=UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        
        User savedUser= userRepo.save(user);
      String emailLink=Helper.getLinkForEmailVerification(emailToken);
      emailService.sendEmail(savedUser.getEmail(), "Verify Account: Email verification for smart contact manager", emailLink);

      return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
       return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
       User user1=userRepo.findById(user.getUserId()).orElseThrow(()->new ResourceNotFoundException("User not found"));
       user1.setName(user.getUsername());
       user1.setEmail(user.getEmail());
       user1.setPassword(user.getPassword());
       user1.setPhoneNumber(user.getPhoneNumber());
       user1.setAbout(user.getAbout());
       user1.setProfilePic(user.getProfilePic());
       user1.setEnabled(user.isEnabled());
       user1.setEmailVerified(user.isEmailVerified());
       user1.setPhoneVerified(user.isPhoneVerified());
       user1.setProvider(user.getProvider());
         user1.setProviderId(user.getProviderId());
         return Optional.of(userRepo.save(user1));

    }

    @Override
    public void deleteUser(String id) {
        User user1=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
         userRepo.delete(user1);
    }

    @Override
    public boolean isUserExist(String id) {
        return userRepo.existsById(id);
    }

    @Override
    public boolean isUserExitByEmail(String email) {
        User user= userRepo.findByEmail(email).orElse(null);
       return user!=null;
    }

    @Override
    public List<User> AllUsers() {
       return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
       return userRepo.findByEmail(email).orElse(null);
    }
    
}

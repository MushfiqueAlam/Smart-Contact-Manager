package com.scm.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;




@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UserServices userServices;

    // Add a new request mapping for /home
    @GetMapping("/")    
    public String homePage(){
        System.out.println("Inside Home Page");
        return "redirect:/home";
    }

    @RequestMapping(value = "/home", method=RequestMethod.GET)
    public String getName(Model model) {
        System.out.println("Inside Controller");
       model.addAttribute("name", "Mushfique");
       model.addAttribute("git", "yes this is git hub link");
       model.addAttribute("gitlink", "https://github.com/MushfiqueAlam");
        return "home";
    }

    // Add a new request mapping for /about
    @RequestMapping(value = "/about", method=RequestMethod.GET)    
    public String aboutPage(){
        System.out.println("Inside About Page");
        return "about";
    }

    // Add a new request mapping for /service
    @RequestMapping(value = "/services", method=RequestMethod.GET)
    public String servicePage(){
        System.out.println("Inside Service Page");
        return "services";
    }    

    // Add a new request mapping for /contact
    @RequestMapping(value = "/contact", method=RequestMethod.GET)
    public String contactPage(){
        System.out.println("Inside Contact Page");
        return "contact";
    }

    // Add a new request mapping for /login
    @RequestMapping(value = "/login", method=RequestMethod.GET)
    public String loginPage(){
        System.out.println("Inside Login Page");
        return "login";
    }

    // Add a new request mapping for /register
    @RequestMapping(value = "/register", method=RequestMethod.GET)
    public String registerPage(Model model){
        UserForm userForm = new UserForm();
        // userForm.setUserName("Mushfique");
        // userForm.setEmail("mushfiqu@123");
        model.addAttribute("userForm", userForm);
        return "register";
    }

    // Add a new request mappin for do register
    @RequestMapping(value = "/do-register", method=RequestMethod.POST)
    public String doRegister(@Valid @ModelAttribute UserForm userForm ,BindingResult rBindingResult, HttpSession session){
        System.out.println("Inside Do Register Page");
        // fetch data from request
        System.out.println(userForm);
        //validate form data
        if(rBindingResult.hasErrors()){
            System.out.println("Error in form data");
            return "register";
        }
        // save data to database
        // User user =User.builder().userName(userForm.getUserName()).email(userForm.getEmail())
        // .password(userForm.getPassword()).phoneNumber(userForm.getPhoneNumber()).about(userForm.getAbout())
        // .profilePic(null)
        // .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        user.setEnabled(false);
        String profilePicUrl = fetchGoogleProfilePicture(userForm.getEmail());
        user.setProfilePic(profilePicUrl!=null?profilePicUrl:"https://www.pngkit.com/png/detail/126-1262807_instagram-default-profile-picture-png.png");

       User userSaved= userServices.saveUser(user);
       System.out.println(userSaved); 
        // message successully registered
       Message message =Message.builder().content("Successfully Registered").type(MessageType.green).build();
        session.setAttribute("message", message);

        // return to register page
        return "redirect:/register";
    }
    private String fetchGoogleProfilePicture(String email) {
    try {
        String googleApiUrl = "https://www.googleapis.com/oauth2/v2/userinfo?alt=json&email=" + email;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(googleApiUrl, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().get("picture").toString();
        }
    } catch (Exception e) {
        System.out.println("Error fetching profile picture: " + e.getMessage());
    }
    return null; // Return null if fetching fails
}


}

package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;




@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private UserServices userServices;

    //add contact handler
    @RequestMapping( "/add")   
    public String addContactView(Model model){
        ContactForm contactForm=new ContactForm();
        // contactForm.setName("Mushfique");
        // contactForm.setFavorite(true);
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)   
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result, Authentication authentication,HttpSession session){

        //process the form data

        //Validate form
        if(result.hasErrors()){
             session.setAttribute("message",Message.builder().content("Fill the form in proper formate").type(MessageType.red).build());
            return "user/add_contact";
        }

        String userName=Helper.getEmailOfLoggedInUser(authentication);
       User user= userServices.getUserByEmail(userName);

        Contact contact=new Contact();
        contact.setName(contactForm.getName());
        contact.setFavourite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhone(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());

        contactService.save(contact);

        System.out.println(contactForm);
         session.setAttribute("message",Message.builder().content("You have successfully added a new contact").type(MessageType.green).build());
       

        return "redirect:/user/contacts/add";
    }
    
}

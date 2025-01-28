package com.scm.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.entities.providers;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstant;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
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

    @Autowired
    private ImageService imageService;

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
        //upload image code
        String fileName=UUID.randomUUID().toString();

        String fileUrl=imageService.uploadImage(contactForm.getContactImage(),fileName);
        

        Contact contact=new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhone(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPicture(fileUrl);
        contact.setCloudinaryImagePublicId(fileName);

        contactService.save(contact);

        System.out.println(contactForm);
         session.setAttribute("message",Message.builder().content("You have successfully added a new contact").type(MessageType.green).build());
       

        return "redirect:/user/contacts/add";
    }

    //View contacts handler

    @RequestMapping
    public String viewContacts(@RequestParam(value = "page",defaultValue = "0") int page,
     @RequestParam(value = "size",defaultValue = AppConstant.PAZE_SIZE+"") int size,
     @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
     @RequestParam(value = "direction",defaultValue = "asc") String direction, 
      Model model,Authentication authentication){
        //load all contacts
       String userName= Helper.getEmailOfLoggedInUser(authentication);
       User user=userServices.getUserByEmail(userName);
       
      Page<Contact> pageContact= contactService.getByUser(user,page,size,sortBy,direction);
      model.addAttribute("pageContact", pageContact);
      model.addAttribute("pageSize",AppConstant.PAZE_SIZE);

        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }

    // Search Handler 

    @RequestMapping("/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm,
     @RequestParam(value = "size",defaultValue = AppConstant.PAZE_SIZE+"") int size,
     @RequestParam(value = "page",defaultValue = "0") int page,
     @RequestParam(value = "sortBy",defaultValue = "name")String sortBy,
     @RequestParam(value = "direction",defaultValue = "asc")String direction,
     Model model,Authentication authentication
     ){

        var user=userServices.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact=null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContact=contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            pageContact=contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("phoneNumber")){
            pageContact=contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstant.PAZE_SIZE);

        System.out.println(pageContact);

        return "user/search";
    }
    
}
